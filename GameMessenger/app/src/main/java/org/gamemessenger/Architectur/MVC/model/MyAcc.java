package org.gamemessenger.Architectur.MVC.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyAcc {

    @SerializedName("uniques_id")
    @Expose
    private String uniques_id;

    @SerializedName("nick")
    @Expose
    private String nick;

    @SerializedName("firebase_token")
    @Expose
    private String firebase_token;

    @SerializedName("error")
    @Expose
    private boolean error;

    public MyAcc(String uniques_id,String nick,String firebase_token) {
        this.uniques_id = uniques_id;
        this.nick = nick;
        this.firebase_token = firebase_token;
    }


    public String getUid() {
        return uniques_id;
    }

    public String getNick() {
        return nick;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public boolean isError() {
        return error;
    }



    @NonNull
    @Override
    public String toString() {
        return uniques_id + " " + nick + " " + firebase_token;
    }
}
