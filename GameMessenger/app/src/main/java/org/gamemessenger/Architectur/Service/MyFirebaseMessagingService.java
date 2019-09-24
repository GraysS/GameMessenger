package org.gamemessenger.Architectur.Service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.gamemessenger.Architectur.MVC.model.Friends;
import org.gamemessenger.Architectur.MVC.model.MessageFriends;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel.DBLocalFriends;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel.DBLocalMessageFriends;
import org.gamemessenger.Architectur.MVC.model.helper.Manager.LocationMyActivityManager;
import org.gamemessenger.Architectur.MVC.model.helper.NotificationUtils;
import org.gamemessenger.Architectur.app.Config;
import org.gamemessenger.ValidCode.Activity.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private LocationMyActivityManager locationMyActivityManager;
    private DBLocalFriends dbLocalFriends;
    private DBLocalMessageFriends dbLocalMessageFriends;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        locationMyActivityManager = new LocationMyActivityManager(getApplicationContext());

        if(locationMyActivityManager.isLocation()) {
            return;
        }

        if (remoteMessage == null)
            return;
        // Check if message contains a notification payload.
       /* if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        //    handleNotification(remoteMessage.getNotification().getBody());
        }*/
       if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

           try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());

                JSONObject data = json.getJSONObject("data");

              /* JSONObject ars = null;
               if(data.has("notification")) {
                   ars = data.getString("notification");
               }*/

               String type = null;
               JSONObject notificationPush = null;
               if(data.has("notification")) {
                   JSONObject jsonArray = data.getJSONObject("notification");
                   notificationPush = jsonArray.getJSONObject("data");
                   type = notificationPush.getString("type");
               } else {
                   type = data.getString("type");
               }
                switch (type) {
                   case Config.TYPE_MessageFriendsAddMessageActivity:
                       Log.i(TAG, String.valueOf(remoteMessage.getData().size()));
                       handleDataMessageFriendsAdd(
                               data.getInt("id"),
                               data.getInt("user_id"),
                               data.getString("last_message"),
                               data.getString("frId"),
                               data.getLong("message_time"),
                               data.getString("notifSubFriends"),
                               data.getString("nick"),
                               notificationPush.getString("title"),
                               notificationPush.getString("message"),
                               notificationPush.getString("date"),
                               notificationPush.getString("notifSub")
                               );
                       break;
                    case Config.TYPE_FriendsDeleteActivity:
                        Log.i(TAG, String.valueOf(remoteMessage.getData().size()));
                        handleDataFriendsDelete(data.getInt("user_id"));
                        break;
                    case Config.TYPE_FriendsAddActivity:
                        Log.i(TAG, String.valueOf(remoteMessage.getData().size()));
                        handleDataFriendsAdd(data.getInt("user_id"),
                                data.getString("last_message"),
                                data.getString("frId"),
                                data.getLong("last_message_time"),
                                data.getString("notifSubFriends"),
                                data.getString("nick"),
                                notificationPush.getString("title"),
                                notificationPush.getString("message"),
                                notificationPush.getString("date"),
                                notificationPush.getString("notifSub")
                                );
                        break;
                    case Config.TYPE_MessageFriendsDeleteMessageActivity:
                        Log.i(TAG, String.valueOf(remoteMessage.getData().size()));
                        handleDataMessageFriendsDelete(
                                data.getInt("user_id"),
                                data.getString("last_message"),
                                data.getString("frId"),
                                data.getLong("message_time"),
                                data.getString("notifSubFriends"),
                                data.getString("nick")
                        );
                        break;

               }
            } catch (JSONException e) {
               e.printStackTrace();
            } catch (Exception e) {
               e.printStackTrace();
            }
       }

    }

    private void handleDataMessageFriendsDelete(int user_id, String last_message, String frId, long message_time, String notifSubFriends, String nick) {
        dbLocalFriends = new DBLocalFriends(getApplicationContext());

        Friends friends = new Friends(user_id,frId,nick,last_message,message_time,notifSubFriends);

        dbLocalFriends.updateFriends(friends);

        Intent intent = new Intent(Config.TYPE_MessageFriendsDeleteMessageActivity);

        intent.putExtra(Friends.class.getCanonicalName(),friends);

        sendBroadcast(intent);


    }

    private void handleDataMessageFriendsAdd(int id, int user_id, String last_message, String frId, long message_time, String subFriends, String nick,
                                   String title, String message, String timeStamp, String notifSubFriends) {

        dbLocalFriends = new DBLocalFriends(getApplicationContext());
        dbLocalMessageFriends = new DBLocalMessageFriends(getApplicationContext());

        Friends friends = new Friends(user_id,frId,nick,last_message,message_time,subFriends);

        dbLocalFriends.updateFriends(friends);

        /*

         ТУТ КОД ПРОВЕРКА И ВЫЗОВА НА ВСЕ СООБЩЕНИЯ ЕСЛИ ТОК ЗАШЕЛ В FGCTACTIVITY


         */
        MessageFriends messageFriends = new MessageFriends(id,frId,message,message_time);

        dbLocalMessageFriends.addMessage(messageFriends,user_id);

        Intent intent = new Intent(Config.TYPE_MessageFriendsAddMessageActivity);

        intent.putExtra(MessageFriends.class.getCanonicalName(),messageFriends);
        intent.putExtra(Friends.class.getCanonicalName(),friends);

        sendBroadcast(intent);

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
             if(notifSubFriends.equals("TRUE")) {
                 Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                 resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                 notificationUtils = new NotificationUtils(getApplicationContext());
                 notificationUtils.showNotificationMessage(title, message, timeStamp, resultIntent);
                 Log.i(TAG, "showNotificationMessage");
             }
        }
     }

    private void handleDataFriendsDelete(int user_id) {
        dbLocalFriends = new DBLocalFriends(getApplicationContext());

        dbLocalFriends.deleteFriends(user_id);

        Intent intent = new Intent(Config.TYPE_FriendsDeleteActivity);
        intent.putExtra("user_id_delete",user_id);
        sendBroadcast(intent);

    }

    private void handleDataFriendsAdd(int user_id, String last_message,
                                      String frId, long last_message_time,
                                      String notifSubFriends, String nick,
                                      String title,String message,String date,String notifSub) {

        dbLocalFriends = new DBLocalFriends(getApplicationContext());

        Friends friends = new Friends(user_id,frId,nick,last_message,last_message_time,notifSubFriends);

        dbLocalFriends.addFriends(friends);

        Intent intent = new Intent(Config.TYPE_FriendsAddActivity);
        intent.putExtra(Friends.class.getCanonicalName(),friends);
        sendBroadcast(intent);

        if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            if(notifSub.equals("TRUE")) {
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.showNotificationMessage(title, message, date, resultIntent);
            }
        }
        Log.d(TAG,"METHOD_ADD");
    }


}
