package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.network.models.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ComServer implements Runnable {

    private boolean running = false;
    private Config config = new Config();
    private final IGameServer gameServer;
    private final Map<Long, SocketWorker> workers = new HashMap<>();
    private Thread thread;
    private ServerSocket serverSocket;
    private int idCounter = 0;

    public ComServer(IGameServer gameServer) {
        this.gameServer = gameServer;
    }

    public ComServer(Config config, IGameServer gameServer) {
        this.config = config;
        this.gameServer = gameServer;
    }

    public synchronized void startServer() {
        if (running)
            return;
        if (thread != null)
            thread.interrupt();
        thread = new Thread(this);
        try {
            serverSocket = new ServerSocket(config.getPort());
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = true;
    }

    @Override
    public void run() {
        System.out.println("ComServer.run");
        while (running && !Thread.interrupted()) {
            try {
                Socket clientSocket = serverSocket.accept();
                SocketWorker worker = new SocketWorker(++idCounter, clientSocket, gameServer);
                worker.registerBreakageListener(() -> workers.remove(worker.getWID()));
                workers.put(worker.getWID(), worker);
                worker.startWorker();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopServer() {
        if (!running)
            return;
        running = false;
        thread.interrupt();
    }

    public void registerBreakageListener(long wid, Runnable runnable) {
        SocketWorker worker = workers.get(wid);
        if (worker != null)
            worker.registerBreakageListener(runnable);
    }

}
