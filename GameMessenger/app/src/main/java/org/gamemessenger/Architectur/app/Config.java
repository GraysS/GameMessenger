package org.gamemessenger.Architectur.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Config {

    //Encoder
   /* public static final String UTF8 = "UTF-8";

    public static final String TOPIC_GLOBAL = "global";*/

    // broadcast receiver intent filters
    //  public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String TYPE_MessageFriendsAddMessageActivity = "MessageFriendsAddMessageActivity";
    public static final String TYPE_MessageFriendsDeleteMessageActivity = "MessageFriendsDeleteMessageActivity";

    //MainActivity (Friend)
    public static final String TYPE_FriendsDeleteActivity = "FriendsDeleteActivity";
    public static final String TYPE_FriendsAddActivity = "FriendsAddActivity";

   // public static final String PUSH_NOTIFICATION = "pushNotification";


    public static boolean isMyServiceRunning(Class<?> serviceClass,Context context,String TAG) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "WORKS");
                return true;
            } else {
                context.startService(new Intent(context,serviceClass.getClass()));
                Log.i(TAG, "NO");
                return false;
            }
        }
        return false;
    }

    // id to handle the notification in the notification tray

  //  public static final String SHARED_PREF = "ah_firebase";
}
