package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.network.command.Command;
import ir.soroushtabesh.hearthstone.network.models.Message;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.JSONUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Collections;
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
    private final Map<Long, LazyResult<Packet>> filters = Collections.synchronizedMap(new HashMap<>());

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
                s = inputStream.readUTF();
                Packet packet = JSONUtil.getGson().fromJson(s, Packet.class);
                Command command = packet.getCommand();
                if (command != null) {
                    new Thread(() -> {
                        Message message = command.visit(this, gameServer, packet.getPID());
                        sendPacket(new Packet(message), packet.getPID());
                    }).start();
                }
                LazyResult<Packet> lazyResult = filters.remove(packet.getPID());
                if (lazyResult != null)
                    new Thread(() -> lazyResult.call(packet)).start();
            } catch (IOException e) {
                reportBreakage();
                break;
            } catch (Exception ignored) {
            }
        }
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
            outputStream.writeUTF(JSONUtil.getGson().toJson(packet));
        } catch (IOException e) {
            e.printStackTrace();
            reportBreakage();
        }
    }

    public void receivePacket(long pid, LazyResult<Packet> result) {
        filters.put(pid, result);
    }

    public Packet receivePacket(long pid) {
        Object lock = new Object();
        AtomicReference<Packet> reference = new AtomicReference<>();
        receivePacket(pid, packet -> {
            reference.set(packet);
            synchronized (lock) {
                lock.notifyAll();
            }
        });
        synchronized (lock) {
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
