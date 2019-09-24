package org.gamemessenger.Architectur.REST.I;

import org.gamemessenger.Architectur.MVC.model.Friends;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IFriends {

    @POST("server/gamemessenger/API/IFriends/AddFriendsNotification.php")
    @FormUrlEncoded
    Call<Friends> addFriendsNotification(@Field("my_id") String my_id,@Field("friends_id") String friends_id);

    @POST("server/gamemessenger/API/IFriends/getFriends.php")
    @FormUrlEncoded
    Call<Friends> getFriends(@Field("my_id") String my_id);

    @POST("server/gamemessenger/API/IFriends/deleteFriends.php")
    @FormUrlEncoded
    Call<Friends> deleteFriends(@Field("my_id") String my_id, @Field("friends_id") int friends_id);
}
