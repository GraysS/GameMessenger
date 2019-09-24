package org.gamemessenger.Architectur.REST.I;

import org.gamemessenger.Architectur.MVC.model.Notification;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface INotification {

    @POST("server/gamemessenger/API/INotification/getNotification.php")
    @FormUrlEncoded
    Call<Notification> getNotification(@Field("my_id") String my_id);

}
