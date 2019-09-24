package org.gamemessenger.Architectur.MVC.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageFriends implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("frId")
    @Expose
    private String frId;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("message_time")
    @Expose
    private long message_time;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("notifSubFriends")
    @Expose
    private String notifSubFriends;

    @SerializedName("nick")
    @Expose
    private String nick;


    @SerializedName("listMessageFriends")
    @Expose
    private List<MessageFriends> listMessageFriends;

    @SerializedName("listThreadMessageFriends")
    @Expose
    private List<MessageFriends> listThreadMessageFriends;

    public MessageFriends(int id, String frId, String message, long message_time) {
        this.id = id;
        this.frId = frId;
        this.message = message;
        this.message_time = message_time;
    }

    public MessageFriends(Parcel in) {
        id = in.readInt();
        frId = in.readString();
        message = in.readString();
        message_time = in.readLong();
    }

    public int getId() {
        return id;
    }

    public String getFrId() {
        return frId;
    }

    public String getMessage() {
        return message;
    }

    public long getMessage_time() {
        return message_time;
    }

    public boolean isError() {
        return error;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getNotifSubFriends() {
        return notifSubFriends;
    }

    public String getNick() {
        return nick;
    }

    public List<MessageFriends> getListMessageFriends() {
        return listMessageFriends;
    }

    public List<MessageFriends> getListThreadMessageFriends() {
        return listThreadMessageFriends;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(frId);
        dest.writeString(message);
        dest.writeLong(message_time);
    }

    public static final Creator<MessageFriends> CREATOR = new Creator<MessageFriends>() {
        @Override
        public MessageFriends createFromParcel(Parcel in) {

            return new MessageFriends(in);
        }

        @Override
        public MessageFriends[] newArray(int size) {

            return new MessageFriends[size];
        }
    };
}
