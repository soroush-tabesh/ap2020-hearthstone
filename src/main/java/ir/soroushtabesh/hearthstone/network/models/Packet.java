package ir.soroushtabesh.hearthstone.network.models;

import ir.soroushtabesh.hearthstone.network.command.Command;
import ir.soroushtabesh.hearthstone.util.JSONUtil;

public class Packet {
    public enum Message {
        SUCCESS, ERROR, WRONG, WAIT, REQ, RES
    }

    private long pid;
    private Message message;
    private String commandClass;
    private String commandData;

    public Packet(Message message, Command command) {
        this(command);
        this.message = message;
    }

    public Packet(Message message) {
        this.message = message;
    }

    public Packet(Command command) {
        this.commandClass = command.getClass().getName();
        this.commandData = JSONUtil.getGson().toJson(command);
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getPID() {
        return pid;
    }

    public Message getMessage() {
        return message;
    }

    @SuppressWarnings("unchecked")
    public Command getCommand() {
        Class<? extends Command> clz;
        try {
            clz = (Class<? extends Command>) getClass().getClassLoader().loadClass(commandClass);
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
        return JSONUtil.getGson().fromJson(commandData, clz);
    }
}
