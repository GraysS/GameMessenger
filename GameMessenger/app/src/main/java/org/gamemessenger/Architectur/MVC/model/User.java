package org.gamemessenger.Architectur.MVC.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("uniques_id")
    @Expose
    private String uniques_id;

    @SerializedName("nick")
    @Expose
    private String nick;


    @SerializedName("error")
    @Expose
    private boolean error;


    @SerializedName("listUsers")
    @Expose
    private List<User> userList;



    public String getUid() {
        return uniques_id;
    }

    public String getNick() {
        return nick;
    }



    public boolean isError() {
        return error;
    }

    public List<User> getUserList() {
        return userList;
    }



    public void setNick(String nick) {
        this.nick = nick;
    }
}
