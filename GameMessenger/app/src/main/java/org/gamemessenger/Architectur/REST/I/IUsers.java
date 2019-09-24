package org.gamemessenger.Architectur.REST.I;

import org.gamemessenger.Architectur.MVC.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IUsers {

    @POST("server/gamemessenger/API/IUsers/getListUsers.php")
    @FormUrlEncoded
    Call<User> getListUsers(@Field("id") String uniques_id);

    @POST("server/gamemessenger/API/IUsers/isNotificationUser.php")
    @FormUrlEncoded
    Call<User> isNotificationUser(@Field("my_id") String my_id,@Field("friends_id") String friends_id);

    @POST("server/gamemessenger/API/IUsers/AddNotificationUser.php")
    @FormUrlEncoded
    Call<User> addNotificationUser(@Field("my_id") String my_id,@Field("friends_id") String friends_id);


}
