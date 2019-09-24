package org.gamemessenger.Architectur.REST.I;

import org.gamemessenger.Architectur.MVC.model.MessageFriends;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMessageFriends {

    @POST("server/gamemessenger/API/IMessageFriends/AddMessageFriends.php")
    @FormUrlEncoded
    Call<MessageFriends> addMessageFriends(@Field("my_id") String my_id, @Field("friends_id") int friends_id, @Field("message") String message, @Field("dates") long dates);


    @POST("server/gamemessenger/API/IMessageFriends/getMessageFriends.php")
    @FormUrlEncoded
    Call<MessageFriends> getMessageFriends(@Field("my_id") String my_id, @Field("friends_id") int friends_id);


    @POST("server/gamemessenger/API/IMessageFriends/getThreadMessageFriends.php")
    @FormUrlEncoded
    Call<MessageFriends> getThreadMessageFriends(@Field("my_id") String my_id, @Field("friends_id") String friends_id);

    @POST("server/gamemessenger/API/IMessageFriends/deleteMessageFriends.php")
    @FormUrlEncoded
    Call<MessageFriends> deleteMessageFriends(@Field("my_id") String my_id, @Field("friends_id") int friends_id, @Field("id") int id);


}
