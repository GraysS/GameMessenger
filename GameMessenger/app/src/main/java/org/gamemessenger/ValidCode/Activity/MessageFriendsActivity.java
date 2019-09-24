package org.gamemessenger.ValidCode.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.gamemessenger.Architectur.Factory.SingleActivity;
import org.gamemessenger.Architectur.MVC.model.Friends;
import org.gamemessenger.Architectur.MVC.model.MessageFriends;
import org.gamemessenger.Architectur.MVC.model.MyAcc;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel.DBLocalFriends;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel.DBLocalMessageFriends;
import org.gamemessenger.Architectur.MVC.model.helper.NotificationUtils;
import org.gamemessenger.Architectur.REST.I.IMessageFriends;
import org.gamemessenger.Architectur.REST.Singleton.Client;
import org.gamemessenger.Architectur.REST.Singleton.MyAccSingle;
import org.gamemessenger.Architectur.adapter_android.MessageFriendsAdapter;
import org.gamemessenger.Architectur.adapter_android.OnMessageFriendsClickListener;
import org.gamemessenger.Architectur.app.Config;
import org.gamemessenger.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFriendsActivity extends SingleActivity {

    private TextView tv_friends_nick;
    private ImageButton ib_sendMessage;
    private EditText  et_message;
    private RecyclerView messageRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    private MessageFriendsAdapter messageFriendsAdapter;

    private IMessageFriends myGetMessage;

    private Bundle bundle;

    private BroadcastReceiver upd_res;

    private DBLocalFriends dbLocalFriends;
    private DBLocalMessageFriends dbLocalMessageFriends;

    private Friends myFriends;

    private MyAcc myAcc;

    private static final String TAG = MessageFriendsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_Message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myGetMessage = Client.getIMessageUser();

        tv_friends_nick = (TextView) findViewById(R.id.tv_friends_nick);
        ib_sendMessage = (ImageButton) findViewById(R.id.ib_sendMessage);
        et_message = (EditText) findViewById(R.id.et_message);

        messageRecyclerView = findViewById(R.id.rv_message);

        myAcc = MyAccSingle.getInstance(getApplicationContext()).getAcc();

        myFriends = (Friends) getIntent().getParcelableExtra(Friends.class.getCanonicalName());

        dbLocalFriends = new DBLocalFriends(getApplicationContext());
        dbLocalMessageFriends = new DBLocalMessageFriends(getApplicationContext());

        Log.d("my_id", myAcc.getUid());
        Log.d("id_friends", String.valueOf(myFriends.getUid()));

        tv_friends_nick.setText(myFriends.getNick());


        messageFriendsAdapter = new MessageFriendsAdapter(new OnMessageFriendsClickListener() {
            @Override
            public void onLongMessageClick(MessageFriends messageUser, int position) {
                onListItemSelect(position, messageFriendsAdapter);
            }

            @Override
            public void onMessageClick(MessageFriends messageUser, int position) {
                if (getmActionMode() != null) {
                    onListItemSelect(position, messageFriendsAdapter);
                }
                Toast.makeText(MessageFriendsActivity.this,"sdd: "+messageUser.getId(),Toast.LENGTH_LONG).show();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setStackFromEnd(true);

        messageRecyclerView.setLayoutManager(linearLayoutManager);

        messageRecyclerView.setAdapter(messageFriendsAdapter);

        ib_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = et_message.getText().toString().trim();
                Log.i("MessageFriendsActivity", myAcc.getUid());
                Log.i("MessageFriendsActivity", String.valueOf(myFriends.getUid()));
                Log.i("MessageFriendsActivity",messageText);
                et_message.setText("");
                myGetMessage.addMessageFriends(myAcc.getUid(),myFriends.getUid(),messageText,new Date().getTime()).enqueue(new Callback<MessageFriends>() {
                        @Override
                        public void onResponse(Call<MessageFriends> call, Response<MessageFriends> response) {
                            //  linearLayoutManager.smoothScrollToPosition(messageRecyclerView,null,messageFriendsAdapter.getItemCount());
                            //new MyAsyncGetMessageFriends().execute();
                            if(!response.body().isError()) {

                                Friends friends = new Friends(
                                        response.body().getUser_id(),
                                        response.body().getFrId(),
                                        response.body().getNick(),
                                        response.body().getMessage(),
                                        response.body().getMessage_time(),
                                        response.body().getNotifSubFriends());

                                dbLocalFriends.updateFriends(friends);

                                MessageFriends messageFriends = new MessageFriends(response.body().getId(),response.body().getFrId(),response.body().getMessage(),response.body().getMessage_time());
                                dbLocalMessageFriends.addMessage(messageFriends,response.body().getUser_id());
                                messageFriendsAdapter.addItems(messageFriends);

                                changeScrollPositionAdapter();
                                Log.i("MessageFriendsActivity", "Ok");
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageFriends> call, Throwable t) {
                            // Toast.makeText(MessageFriendsActivity.this,"ВАС УДАЛИЛИ С ДРУЗЕЙ ИГРА ОКОНЧЕНА ",Toast.LENGTH_LONG).show();
                            Log.e("MessageFriendsActivity" ,"Unable to submit post to API. " + t.getMessage());
                        }
                    });
            }
        });


        if(savedInstanceState != null) {
            bundle = savedInstanceState;
            Log.i("MessageFriendsActivity","dsf");
        }

        upd_res = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Config.TYPE_MessageFriendsAddMessageActivity)) {
                    MessageFriends myMessageFriends = (MessageFriends) intent.getParcelableExtra(MessageFriends.class.getCanonicalName());
                    scrollStartBundle();
                    messageFriendsAdapter.addItems(myMessageFriends);
                    scrollEndBundle();
                    Log.i("MessageFriendsActivity", "onReceive");
                } else if(intent.getAction().equals(Config.TYPE_MessageFriendsDeleteMessageActivity)) {
                   // new MyAsyncGetMessageFriends().execute();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_message_toolbar,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.menu_notification:
                item.setIcon(R.drawable.outline_volume_up_24);
                item.setTitle(R.string.menu_on_notifications);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart(){
        super.onStart();
        registerReceiver(upd_res, new IntentFilter(Config.TYPE_MessageFriendsAddMessageActivity));
        registerReceiver(upd_res,new IntentFilter(Config.TYPE_MessageFriendsDeleteMessageActivity));
        NotificationUtils.clearNotifications(getApplicationContext());
        Log.i("MessageFriendsActivity","OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbLocalMessageFriends.isEmptyListMessage(myFriends.getUid())) {
            new MyAsyncGetMessageFriends().execute();
        } else {
            getListMessageLocal();
        }
        Log.i(TAG, "OnResume");
    }

    @Override
    protected void onStop() {
  /*      Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MessageFriendsActivity","OnStop");
            }
        },10000);*/
        unregisterReceiver(upd_res);
        Log.i("MessageFriendsActivity","OnStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("MessageFriendsActivity","OnDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("state", messageRecyclerView.getLayoutManager().onSaveInstanceState());
    }


    //Delete selected rows
    public void deleteRows() {
        SparseBooleanArray selected = messageFriendsAdapter.getSelectedIds();//Get selected ids

        final MessageFriendsAdapter adapter = (MessageFriendsAdapter) messageRecyclerView.getAdapter();
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                final MessageFriends messageUser = adapter.getItem(selected.keyAt(i));

                myGetMessage.deleteMessageFriends(myAcc.getUid(), myFriends.getUid(),messageUser.getId()).enqueue(new Callback<MessageFriends>() {
                    @Override
                    public void onResponse(Call<MessageFriends> call, Response<MessageFriends> response) {

                        if(!response.body().isError()) {

                            Friends friends = new Friends(
                                    response.body().getUser_id(),
                                    response.body().getFrId(),
                                    response.body().getNick(),
                                    response.body().getMessage(),
                                    response.body().getMessage_time(),
                                    response.body().getNotifSubFriends());

                            dbLocalFriends.updateFriends(friends);

                            dbLocalMessageFriends.deleteMessage(response.body().getUser_id(),messageUser.getId());

                            messageFriendsAdapter.deleteItems(messageUser);
                        }
                        Log.i("MessageFriendsActivity", "GO ");
                    }

                    @Override
                    public void onFailure(Call<MessageFriends> call, Throwable t) {
                        Log.e("MessageFriendsActivity", "Unable to submit post to API. " + t.getMessage());
                    }
                });

            }
        }
        Toast.makeText(this, selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        getmActionMode().finish();
    }

    private void getListMessageLocal() {
        scrollStartBundle();
        messageFriendsAdapter.clearItems();
        messageFriendsAdapter.setItems(dbLocalMessageFriends.getListFriends(myFriends.getUid()));
        messageFriendsAdapter.setMyId(myAcc.getUid());
        scrollEndBundle();
    }



    private void changeScrollPositionAdapter()
    {
        messageRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageRecyclerView.scrollToPosition(messageRecyclerView.getAdapter().getItemCount()-1);
            }
        },100);

    }

    private void scrollStartBundle(){
        int maxScroll = messageRecyclerView.computeVerticalScrollRange();
        int currentScroll = messageRecyclerView.computeVerticalScrollOffset() + messageRecyclerView.computeVerticalScrollExtent();
        if(bundle == null) {
            if (currentScroll == maxScroll) {
                changeScrollPositionAdapter();
            }
        }
    }

    private void scrollEndBundle() {
        if(bundle != null && !bundle.isEmpty()) {
            Parcelable savedRecyclerLayoutState = bundle.getParcelable("state");
            Log.i("MessageFriendsActivity","Work " + savedRecyclerLayoutState);
            messageRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            bundle = null;
        }
    }


    private class MyAsyncGetMessageFriends extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            scrollStartBundle();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myGetMessage.getMessageFriends(myAcc.getUid(),myFriends.getUid()).enqueue(new Callback<MessageFriends>() {
                    @Override
                    public void onResponse(Call<MessageFriends> call, Response<MessageFriends> response) {
                        List<MessageFriends> messageUserList = response.body().getListMessageFriends();

                        if(messageUserList == null) {
                            messageUserList = new ArrayList<>();
                        }

                        if(messageUserList.size() > 0) {
                            Log.d(TAG, String.valueOf(messageUserList.size()));
                            dbLocalMessageFriends.addListMessage(messageUserList,myFriends.getUid());
                        }

                        messageFriendsAdapter.clearItems();
                        messageFriendsAdapter.setItems(messageUserList);
                        messageFriendsAdapter.setMyId(myAcc.getUid());


                        Log.i("MessageFriendsActivity","OKOK");
                    }

                    @Override
                    public void onFailure(Call<MessageFriends> call, Throwable t) {
                        Log.i("MessageFriendsActivity","ERRORR" + t.getMessage());
                    }
                });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            scrollEndBundle();
        }
    }

  /*  private class ResponseHandler extends Handler {
        private Context mContext;

        ResponseHandler(Context context) {
            mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case FoneService.TASK_RESPONSE_1:
                  //  String result = msg.getData().getString("message_res");
                    new MyAsyncGetMessageFriends().execute();
                  //  Toast.makeText(mContext, "Пришло из IPC службы: " + result, Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }*/

   /* public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            Log.i("MessageFriendsActivity",
                    "+ data yes");
            new MyAsyncGetMessageFriends().execute();
        }
    }
*/

}
