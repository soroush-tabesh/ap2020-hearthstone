package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.network.command.Command;
import ir.soroushtabesh.hearthstone.network.models.Config;
import ir.soroushtabesh.hearthstone.network.models.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class RemoteGameServer implements IGameServer {

    private static final RemoteGameServer instance = new RemoteGameServer();
    private Config config = new Config();
    private SocketWorker worker;
    private Socket socket;
    private Long token = null;
    private Runnable listener;

    private RemoteGameServer() {
    }

    public static RemoteGameServer getInstance() {
        return instance;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed() && worker != null && worker.isRunning();
    }

    public boolean connect() {
        if (isConnected())
            return true;
        try {
            socket = new Socket(InetAddress.getByName(config.getAddress()), config.getPort());
            worker = new SocketWorker(0, socket, this);
            if (listener != null)
                worker.registerBreakageListener(listener);
            worker.startWorker();
            System.out.println("RemoteGameServer: connected");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void disconnect() {
        if (socket != null && !socket.isClosed() && worker != null) {
            worker.registerBreakageListener(null);
            worker.stopWorker();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SocketWorker getWorker() {
        return worker;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public void setOnErrorListener(Runnable listener) {
        this.listener = listener;
        if (worker != null)
            worker.registerBreakageListener(listener);
    }

    public static Packet sendPOST(Command command) {
        return getInstance().getWorker()
                .sendAndWaitReceivePacket(new Packet(command));
    }

}
