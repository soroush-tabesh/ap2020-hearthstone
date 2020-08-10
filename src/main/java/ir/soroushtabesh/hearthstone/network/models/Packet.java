package ir.soroushtabesh.hearthstone.network.models;

import ir.soroushtabesh.hearthstone.network.command.Command;
import ir.soroushtabesh.hearthstone.util.JSONUtil;

public class Packet {

    private long pid;
    private Message message;
    private String commandClass;
    private String commandData;
    private String parcelClass;
    private String parcelData;

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

    public void setParcel(Object parcel) {
        parcelClass = parcel.getClass().getName();
        parcelData = JSONUtil.getGson().toJson(parcel);
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

    public Object getParcel() {
        Class<?> clz;
        try {
            clz = getClass().getClassLoader().loadClass(parcelClass);
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
        return JSONUtil.getGson().fromJson(parcelData, clz);
    }

}
