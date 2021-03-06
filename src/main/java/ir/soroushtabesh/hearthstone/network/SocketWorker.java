package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.network.command.Command;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.JSONUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SocketWorker implements Runnable {
    private final long wid;
    private final Socket socket;
    private Thread thread;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final SecureRandom secureRandom = new SecureRandom();
    private final IGameServer gameServer;
    private Runnable listener;
    private final Map<Long, LazyResult<Packet>> filters = new HashMap<>();

    SocketWorker(long wid, Socket socket, IGameServer gameServer) {
        this.wid = wid;
        this.socket = socket;
        this.gameServer = gameServer;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot connect to " + socket.getRemoteSocketAddress());
        }
    }

    public long getWID() {
        return wid;
    }

    public void registerBreakageListener(Runnable runnable) {
        listener = runnable;
    }

    @Override
    public void run() {
        String s;
        while (!Thread.interrupted()) {
            try {
                s = receive();
                Packet packet = JSONUtil.getGson().fromJson(s, Packet.class);
                System.out.println("received: " + packet);
                Command command = packet.getCommand();
                if (command != null) {
                    new Thread(() -> {
                        Packet res = command.visit(this, gameServer, packet.getPID());
                        sendPacket(res, packet.getPID());
                    }).start();
                }
                LazyResult<Packet> lazyResult = filters.get(packet.getPID());
                if (lazyResult != null) {
                    new Thread(() -> lazyResult.call(packet)).start();
                }
            } catch (IOException e) {
                reportBreakage();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                reportBreakage();
                break;
            }
        }
    }

    private String receive() throws IOException {
        byte[] len = readNBytes(4);
        int l = ByteBuffer.wrap(len).getInt();
        System.out.println("rt: " + System.currentTimeMillis());
        System.out.println("received len: " + l);
        return new String(readNBytes(l), StandardCharsets.UTF_8);
    }

    private byte[] readNBytes(int n) throws IOException {
        byte[] res = new byte[n];
        for (int i = 0; i < n; i++) {
            res[i] = inputStream.readByte();
        }
        return res;
    }

    private void reportBreakage() {
        System.err.println("Broken pipe: " + socket.getRemoteSocketAddress());
        if (listener != null)
            listener.run();
    }

    public long generateID() {
        return secureRandom.nextLong();
    }

    public synchronized void sendPacket(Packet packet) {
        sendPacket(packet, generateID());
    }

    public synchronized void sendPacket(Packet packet, long pid) {
        packet.setPid(pid);
        try {
            System.out.println("st: " + System.currentTimeMillis());
            String str = JSONUtil.getGson().toJson(packet);
//            outputStream.writeUTF(str);
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            System.out.println("sent len:  " + bytes.length);
            System.out.println("sent: " + packet);
            outputStream.writeInt(bytes.length);
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            reportBreakage();
        }
    }

    public void registerPacketReceiver(long pid, LazyResult<Packet> result) {
        filters.put(pid, result);
    }

    public void unregisterPacketReceiver(long pid) {
        filters.remove(pid);
    }

    public Packet sendAndWaitReceivePacket(Packet toSend, long pid) {
        Object lock = new Object();
        AtomicReference<Packet> reference = new AtomicReference<>();
        registerPacketReceiver(pid, packet -> {
            reference.set(packet);
            unregisterPacketReceiver(pid);
            synchronized (lock) {
                lock.notifyAll();
            }
        });
        synchronized (lock) {
            sendPacket(toSend, pid);
            while (reference.get() == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return reference.get();
        }
    }

    public Packet sendAndWaitReceivePacket(Packet toSend) {
        return sendAndWaitReceivePacket(toSend, secureRandom.nextLong());
    }

    public boolean isRunning() {
        return thread != null && !thread.isInterrupted() && thread.isAlive();
    }

    public void startWorker() {
        if (thread != null)
            return;
        thread = new Thread(this);
        thread.start();
    }

    public void stopWorker() {
        if (thread == null)
            return;
        thread.interrupt();
    }
}
