package org.gamemessenger.Architectur.REST.Singleton;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import org.gamemessenger.Architectur.REST.I.IFriends;
import org.gamemessenger.Architectur.REST.I.IMessageFriends;
import org.gamemessenger.Architectur.REST.I.IMyAcc;
import org.gamemessenger.Architectur.REST.I.INotification;
import org.gamemessenger.Architectur.REST.I.IUsers;

public class Client {

    private static final String BASE_URL = "http://192.168.42.246/";

    private static Retrofit retrofit = null;

    private static synchronized Retrofit getClient(String baseUrl) {
        if(retrofit == null) {
            retrofit  = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static IMyAcc getIMyAcc() {
        return getClient(BASE_URL).create(IMyAcc.class);
    }

    public static IUsers getIUsers() {
        return getClient(BASE_URL).create(IUsers.class);
    }

    public static IMessageFriends getIMessageUser() {
        return getClient(BASE_URL).create(IMessageFriends.class);
    }

     public static IFriends getIFriends() {
        return getClient(BASE_URL).create(IFriends.class);
    }

    public static INotification getINotification() {
        return getClient(BASE_URL).create(INotification.class);
    }



}
