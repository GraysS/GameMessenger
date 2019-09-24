package org.gamemessenger.ValidCode.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.gamemessenger.Architectur.MVC.model.MyAcc;
import org.gamemessenger.Architectur.MVC.model.User;
import org.gamemessenger.Architectur.REST.I.IUsers;
import org.gamemessenger.Architectur.REST.Singleton.Client;
import org.gamemessenger.Architectur.REST.Singleton.MyAccSingle;
import org.gamemessenger.Architectur.adapter_android.OnUserClickListener;
import org.gamemessenger.Architectur.adapter_android.UsersAdapter;
import org.gamemessenger.Architectur.app.Config;
import org.gamemessenger.R;
import org.gamemessenger.ValidCode.Fragment.MyDialogFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
                implements MyDialogFragment.YesNoListener {

    private UsersAdapter usersAdapter;
    private RecyclerView usersRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private IUsers myGetUsers;

  //  private String uniques_id;
    private String uniques_friends_id;
    private String nick;
    private MyAcc myAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_Search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        uniques_id = getIntent().getStringExtra("uniques_id");

        myAcc = MyAccSingle.getInstance(getApplicationContext()).getAcc();
        Log.i("SearchActivity",myAcc.getUid());

        myGetUsers = Client.getIUsers();

        usersAdapter = new UsersAdapter(new OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                uniques_friends_id = user.getUid();
                nick = user.getNick();
                Log.i("SearchActivity",user.getUid());
                myGetUsers.isNotificationUser(myAcc.getUid(),uniques_friends_id)
                            .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.body().isError()){
                                new MyDialogFragment().show(getSupportFragmentManager(), "tag");
                            } else if(uniques_friends_id.equals(response.body().getUid())){
                                showError(R.string.CheckNotification,R.string.notification_get_error,nick);
                            } else if(myAcc.getUid().equals(response.body().getUid())) {
                                showError(R.string.Okay,R.string.notification_add_error,nick);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.i("SearchActivity","ERRORR" + t.getMessage());
                        }
                    });
            }
        });

        usersRecyclerView = (RecyclerView) findViewById(R.id.rv_users);
        linearLayoutManager = new LinearLayoutManager(this);

        usersRecyclerView.setLayoutManager(linearLayoutManager);

        usersRecyclerView.setAdapter(usersAdapter);

        new MyAsyncGetListUser().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    public void onYesDialog() {
        myGetUsers.addNotificationUser(myAcc.getUid(),uniques_friends_id)
                    .enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    showError(R.string.Okay,R.string.notification_add,nick);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("SearchActivity","ERRORR" + t.getMessage());
                }
            });
    }

    @Override
    public int onMessageDialog() {
        return R.string.addfriends;
    }


    private void showError(final int idAct, int id, String name){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.ll_search),getText(id)+" "+ name,Snackbar.LENGTH_LONG);
        snackbar.setAction(idAct, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idAct == R.string.CheckNotification){
                    Intent i = new Intent(SearchActivity.this,NotificationActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        snackbar.show();
    }

    class MyAsyncGetListUser extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
                myGetUsers.getListUsers(myAcc.getUid()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        List<User> userList = response.body().getUserList();
                        if(userList == null){
                            userList = new ArrayList<>();
                        }
                        usersAdapter.clearItems();
                        usersAdapter.setItems(userList);
                        usersAdapter.notifyDataSetChanged();
                        Log.i("SearchActivity","OKOK");
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i("SearchActivity","ERRORR" + t.getMessage());
                    }
                });
            return null;
        }
    }
}
