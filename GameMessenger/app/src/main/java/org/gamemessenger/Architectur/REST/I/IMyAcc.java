package org.gamemessenger.Architectur.REST.I;

import org.gamemessenger.Architectur.MVC.model.MyAcc;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyAcc {
    @POST("server/gamemessenger/API/IMyAcc/login.php")
    @FormUrlEncoded
    Call<MyAcc> login (@Field("nick") String nick,
                            @Field("password") String password,
                            @Field("firebase_token") String token);

    @POST("server/gamemessenger/API/IMyAcc/registration.php")
    @FormUrlEncoded
    Call<MyAcc> registration (@Field("nick") String nick,
                              @Field("password") String password,
                              @Field("firebase_token") String token);


}
