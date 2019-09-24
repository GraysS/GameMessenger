package org.gamemessenger.Architectur.MVC.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Friends implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("frId")
    @Expose
    private String frId;

    @SerializedName("nick")
    @Expose
    private String nick;

    @SerializedName("last_message")
    @Expose
    private String last_message;

    @SerializedName("last_message_time")
    @Expose
    private long last_message_time;

    @SerializedName("notifSubFriends")
    @Expose
    private String notifSubFriends;

    private int count;


    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("listFriends")
    @Expose
    private List<Friends> friendsList;


    public Friends(int user_id, String frId, String nick, String last_message, long last_message_time,
                   String notifSubFriends) {
        this.user_id = user_id;
        this.frId = frId;
        this.nick = nick;
        this.last_message = last_message;
        this.last_message_time = last_message_time;
        this.notifSubFriends = notifSubFriends;
    }

    public Friends() {
    }

    protected Friends(Parcel in) {
        user_id = in.readInt();
        frId = in.readString();
        nick = in.readString();
        last_message = in.readString();
        last_message_time = in.readLong();
        notifSubFriends = in.readString();
       // friendsList = in.createTypedArrayList(Friends.CREATOR);
    }


    public int getUid() {
        return user_id;
    }

    public String getFrId() {
        return frId;
    }

    public String getNick() {
        return nick;
    }

    public String getLast_message() {
        return last_message;
    }

    public long getLast_message_time() {
        return last_message_time;
    }

    public String getNotifSubFriends() {
        return notifSubFriends;
    }

    public int getCount() {
        return count;
    }

    public boolean isError() {
        return error;
    }

    public List<Friends> getFriendsList() {
        return friendsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(frId);
        dest.writeString(nick);
        dest.writeString(last_message);
        dest.writeLong(last_message_time);
        dest.writeString(notifSubFriends);
    }

    public static final Creator<Friends> CREATOR = new Creator<Friends>() {
        @Override
        public Friends createFromParcel(Parcel in) {

            return new Friends(in);
        }

        @Override
        public Friends[] newArray(int size) {

            return new Friends[size];
        }
    };
}
