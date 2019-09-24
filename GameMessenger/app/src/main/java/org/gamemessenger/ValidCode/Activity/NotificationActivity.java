package org.gamemessenger.ValidCode.Activity;

import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.gamemessenger.Architectur.MVC.model.Friends;
import org.gamemessenger.Architectur.MVC.model.MyAcc;
import org.gamemessenger.Architectur.MVC.model.Notification;
import org.gamemessenger.Architectur.MVC.model.User;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel.DBLocalFriends;
import org.gamemessenger.Architectur.REST.I.IFriends;
import org.gamemessenger.Architectur.REST.I.INotification;
import org.gamemessenger.Architectur.REST.I.IUsers;
import org.gamemessenger.Architectur.REST.Singleton.Client;
import org.gamemessenger.Architectur.REST.Singleton.MyAccSingle;
import org.gamemessenger.Architectur.adapter_android.NotificationAdapter;
import org.gamemessenger.Architectur.adapter_android.OnNotificationClickListener;
import org.gamemessenger.Architectur.adapter_android.OnUserClickListener;
import org.gamemessenger.Architectur.app.Config;
import org.gamemessenger.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private NotificationAdapter notificationAdapter;
    private RecyclerView notificationRecyclerView;

    private INotification myGetNotification;
    private IFriends myAddFriends;

    private MyAcc myAcc;

    private static final String TAG = NotificationActivity.class.getSimpleName();
   // private String uniques_id;

    DBLocalFriends dbLocalFriends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_Notification);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAcc = MyAccSingle.getInstance(getApplicationContext()).getAcc();
        dbLocalFriends = new DBLocalFriends(getApplicationContext());

        Log.i("NotificationActivity", myAcc.getUid());

        myGetNotification = Client.getINotification();
        myAddFriends = Client.getIFriends();

       OnNotificationClickListener onBtnAddListener = new OnNotificationClickListener() {
            @Override
            public void onNotificationClick(Notification notification) {
                Log.i("NotificationActivity",notification.getNotif_id());

                myAddFriends.addFriendsNotification(myAcc.getUid(),notification.getNotif_id())
                            .enqueue(new Callback<Friends>() {
                        @Override
                        public void onResponse(Call<Friends> call, Response<Friends> response) {
                            if(!response.body().isError()) {
                                int user_id = response.body().getUid();
                                String frId = response.body().getFrId();
                                String nick = response.body().getNick();
                                String last_message = response.body().getLast_message();
                                long last_message_time = response.body().getLast_message_time();
                                String notifSubFriends = response.body().getNotifSubFriends();

                                Friends myFriends = new Friends(user_id, frId, nick, last_message, last_message_time, notifSubFriends);
                                dbLocalFriends.addFriends(myFriends);

                                Log.i(TAG, myFriends.getUid() + " " + myFriends.getFrId() + " " + myFriends.getNick() + " " + myFriends.getLast_message() + " " + myFriends.getLast_message_time() + " " + myFriends.getNotifSubFriends());
                                Toast.makeText(NotificationActivity.this, "Add", Toast.LENGTH_LONG).show();
                            }
                            new MyAsyncGetNotificationUser().execute();
                        }

                        @Override
                        public void onFailure(Call<Friends> call, Throwable t) {
                            Log.i("NotificationActivity","ERRORR" + t.getMessage());
                        }
                    });
            }
        };

       OnNotificationClickListener onBtnSpamListener = new OnNotificationClickListener() {
           @Override
           public void onNotificationClick(Notification notification) {

           }
       };

        OnNotificationClickListener onBtnCancelListener = new OnNotificationClickListener() {
            @Override
            public void onNotificationClick(Notification notification) {

            }
        };


        notificationAdapter = new NotificationAdapter(onBtnAddListener,onBtnSpamListener,onBtnCancelListener);

        notificationRecyclerView = (RecyclerView) findViewById(R.id.rv_notification);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationRecyclerView.setAdapter(notificationAdapter);


        new MyAsyncGetNotificationUser().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

  private class MyAsyncGetNotificationUser extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            myGetNotification.getNotification(myAcc.getUid()).enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                        List<Notification> notificationList = response.body().getNotificationList();
                        if(notificationList == null){
                            notificationList = new ArrayList<>();
                        }
                        notificationAdapter.clearItems();
                        notificationAdapter.setItems(notificationList);
                        Log.i("NotificationActivity","OKOK");
                    }

                    @Override
                    public void onFailure(Call<Notification> call, Throwable t) {
                        Log.i("NotificationActivity","ERRORR" + t.getMessage());
                    }
                });
            return null;
        }
    }

}
