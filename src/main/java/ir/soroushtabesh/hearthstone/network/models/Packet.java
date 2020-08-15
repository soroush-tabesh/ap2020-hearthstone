package ir.soroushtabesh.hearthstone.network.models;

import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.command.Command;
import ir.soroushtabesh.hearthstone.util.JSONUtil;

import java.io.*;
import java.util.Base64;

public class Packet implements Serializable {

    private static final long serialVersionUID = -8381830815514853403L;
    private long pid;
    private Message message;
    private String commandClass;
    private String commandData;
    private String parcelData;
    private String jsonParcelClass;
    private String jsonParcelData;

    public Packet(Message message, Command command) {
        this(command);
        this.message = message;
    }

    public Packet(Message message) {
        this.message = message;
    }

    public Packet(Command command) {
        setCommand(command);
    }

    public void setCommand(Command command) {
        this.commandClass = command.getClass().getName();
        this.commandData = JSONUtil.getGson().toJson(command);
    }

    public void setParcel(Object parcel) {
        if (parcel == null)
            return;
        parcelData = encode(serialize(parcel));
    }

    public void setJSONParcel(Object parcel) {
        this.jsonParcelClass = parcel.getClass().getName();
        this.jsonParcelData = JSONUtil.getGson().toJson(parcel);
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

    public Command getCommand() {
        try {
            return (Command) parseJSON(commandClass, commandData);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public Object getJSONParcel() {
        return parseJSON(jsonParcelClass, jsonParcelData);
    }

    private static Object parseJSON(String jsonParcelClass, String jsonParcelData) {
        if (jsonParcelClass == null)
            return null;
        try {
            Class<?> clz = Class.forName(jsonParcelClass);
            return JSONUtil.getGson().fromJson(jsonParcelData, clz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getParcel() {
        if (parcelData == null)
            return null;
        return deserialize(decode(parcelData));
    }

    private byte[] serialize(Object object) {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            res = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private Object deserialize(byte[] data) {
        Object res = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            res = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String str) {
        return Base64.getDecoder().decode(str);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "pid=" + pid +
                ", message=" + message +
                ", commandClass='" + commandClass + '\'' +
                ", commandData='" + commandData + '\'' +
                ", parcelData.length='" + (parcelData == null ? 0 : parcelData.length()) + '\'' +
                '}';
    }
}
