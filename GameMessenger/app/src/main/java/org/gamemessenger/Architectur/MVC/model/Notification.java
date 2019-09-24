package org.gamemessenger.Architectur.MVC.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notification {

    @SerializedName("notif_id")
    @Expose
    private String notif_id;

    @SerializedName("nick")
    @Expose
    private String nick;

    @SerializedName("listNotification")
    @Expose
    private List<Notification> notificationList;


    public String getNotif_id() {
        return notif_id;
    }

    public String getNick() {
        return nick;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }
}
