package org.gamemessenger.Architectur.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.gamemessenger.Architectur.MVC.model.MessageFriends;
import org.gamemessenger.Architectur.REST.I.IMessageFriends;
import org.gamemessenger.Architectur.REST.Singleton.Client;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FoneService extends Service {

    Thread thr;
    //DBLocalMyAcc db;
    //HashMap<String,String> friendDb;
    //HashMap<String,String> userDb;
    IMessageFriends myGetThreadMessage;

    boolean isLifeService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLifeService = true;
        Log.i("FoneService","OnCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //db = new DBLocalMyAcc(getApplicationContext());

        myGetThreadMessage = Client.getIMessageUser();

        startLoop(intent.getStringExtra("my_id"),intent.getStringExtra("friends_id"));
    }

     private void startLoop(final String my_id, final String friends_id) {
        thr = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                   if(!isLifeService)
                       break;

                    Log.i("my_id",my_id);
                    Log.i("friends_id",friends_id);
                    myGetThreadMessage.getThreadMessageFriends(my_id,friends_id).enqueue(new Callback<MessageFriends>() {
                        @Override
                        public void onResponse(Call<MessageFriends> call, Response<MessageFriends> response) {
                            List<MessageFriends> listThread =  response.body().getListThreadMessageFriends();
                            if(listThread == null) {
                               listThread = new ArrayList<>();
                            }
                            if(listThread.size() > 0) {
                                sendBroadcast(new Intent(
                                        "by.andreidanilevich.action.UPDATE_RecyclerMessage"));
                                Log.i("chatMy","YES");
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageFriends> call, Throwable t) {
                            Log.i("chatMy","ERRORR" + t.getMessage());

                            //Toast.makeText(this,"ВАС УДАЛИЛИ С ДРУЗЕЙ ИГРА ОКОНЧЕНА ",Toast.LENGTH_LONG).show();
                        }
                    });

                    try {
                        Thread.sleep(3000);
                    } catch (Exception e){
                        Log.i("char","FoneService" + e.getMessage());
                    }
                }
            }
        });

        thr.setDaemon(true);
        thr.start();
    }

    @Override
    public void onDestroy() {
        Log.i("FoneService","OnDestroy");
        isLifeService = false;
        super.onDestroy();
    }
}
