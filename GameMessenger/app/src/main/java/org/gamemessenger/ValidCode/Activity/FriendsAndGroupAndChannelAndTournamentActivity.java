package org.gamemessenger.ValidCode.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.gamemessenger.Architectur.Factory.SingleActivity;
import org.gamemessenger.Architectur.MVC.model.Friends;
import org.gamemessenger.Architectur.MVC.model.MyAcc;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel.DBLocalFriends;
import org.gamemessenger.Architectur.MVC.model.helper.Manager.LocationMyActivityManager;
import org.gamemessenger.Architectur.MVC.model.helper.NotificationUtils;
import org.gamemessenger.Architectur.REST.I.IFriends;
import org.gamemessenger.Architectur.REST.Singleton.Client;
import org.gamemessenger.Architectur.REST.Singleton.MyAccSingle;
import org.gamemessenger.Architectur.Service.MyFirebaseMessagingService;
import org.gamemessenger.Architectur.adapter_android.FriendsAdapter;
import org.gamemessenger.Architectur.adapter_android.OnFriendClickListener;
import org.gamemessenger.Architectur.app.Config;
import org.gamemessenger.R;
import org.gamemessenger.ValidCode.Fragment.MyDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsAndGroupAndChannelAndTournamentActivity extends SingleActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyDialogFragment.YesNoListener  {

    private DBLocalFriends dbLocalFriends;
    private LocationMyActivityManager locationMyActivityManager;
    private IFriends myGetFriends;
    private MyAcc myAcc;

    private FriendsAdapter friendsAdapter;
    private RecyclerView friendsRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView notificationTextView;
    private TextView tv_nick;
    private DrawerLayout drawer;

    private boolean isOneClick = true;

    private BroadcastReceiver upd_res;

    private static final String TAG = FriendsAndGroupAndChannelAndTournamentActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsgroupchanneltournament);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Config.isMyServiceRunning(MyFirebaseMessagingService.class,getApplicationContext(),TAG);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        tv_nick = (TextView) header.findViewById(R.id.tv_nick);

        myAcc = MyAccSingle.getInstance(getApplicationContext()).getAcc();

        tv_nick.setText(myAcc.getNick());

        notificationTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notifications));

        initialiseCountText();

        locationMyActivityManager = new LocationMyActivityManager(getApplicationContext());

        locationMyActivityManager.createLocationOrUpdate("MainActivity");

        dbLocalFriends = new DBLocalFriends(getApplicationContext());

        myGetFriends = Client.getIFriends();


        friendsAdapter = new FriendsAdapter(new OnFriendClickListener() {
            @Override
            public void onFriendClick(View v, Friends friends, int position) {
                if (getmActionMode() != null) {
                    onListItemSelect(position, friendsAdapter);
                } else {
                    if(isOneClick()) {
                        Intent intent = new Intent(FriendsAndGroupAndChannelAndTournamentActivity.this, MessageFriendsActivity.class);
                        intent.putExtra(Friends.class.getCanonicalName(),friends);
                        startActivityForResult(intent,0);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
            }

            @Override
            public void onLongFriendClick(View v,Friends friends, int position) {
                onListItemSelect(position, friendsAdapter);
            }
        });

        friendsRecyclerView = (RecyclerView) findViewById(R.id.rv_friends);
        linearLayoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.addItemDecoration(new DividerItemDecoration(friendsRecyclerView.getContext(), linearLayoutManager.getOrientation()));

        friendsRecyclerView.setLayoutManager(linearLayoutManager);
        friendsRecyclerView.setAdapter(friendsAdapter);

        upd_res = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(friendsAdapter.getItemCount() == 0) {
                    getListFriendsLocal();
                } else {
                    if(intent.getAction().equals(Config.TYPE_FriendsDeleteActivity)) {
                        int user_id = intent.getIntExtra("user_id_delete",-1);
                        friendsAdapter.deleteFriendItems(user_id);
                    } else if(intent.getAction().equals(Config.TYPE_FriendsAddActivity)) {
                       Friends myFriends = (Friends) intent.getParcelableExtra(Friends.class.getCanonicalName());
                       friendsAdapter.addItems(myFriends);
                    } else if(intent.getAction().equals(Config.TYPE_MessageFriendsAddMessageActivity)) {
                        Friends myFriends = (Friends) intent.getParcelableExtra(Friends.class.getCanonicalName());
                        friendsAdapter.updateItems(myFriends);
                    } else if(intent.getAction().equals(Config.TYPE_MessageFriendsDeleteMessageActivity)) {
                        Friends myFriends = (Friends) intent.getParcelableExtra(Friends.class.getCanonicalName());
                        friendsAdapter.updateItems(myFriends);
                    }
                }
            }
        };
}

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(upd_res, new IntentFilter(Config.TYPE_FriendsDeleteActivity));
        registerReceiver(upd_res, new IntentFilter(Config.TYPE_FriendsAddActivity));
        registerReceiver(upd_res,new IntentFilter(Config.TYPE_MessageFriendsAddMessageActivity));
        registerReceiver(upd_res,new IntentFilter(Config.TYPE_MessageFriendsDeleteMessageActivity));
        NotificationUtils.clearNotifications(getApplicationContext());

        Log.i("MainActivity", "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isOneClick)
            isOneClick = true;

        if (dbLocalFriends.isEmptyListFriends()) {
            new MyAsyncGetListFriendsNetwork().execute();
        } else {
            getListFriendsLocal();
        }
        Log.i(TAG, "OnResume");
    }

    @Override
    protected void onStop() {
        unregisterReceiver(upd_res);
        Log.i(TAG, "OnStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
      /*  unregisterReceiver(upd_res);
        Log.i(TAG, "OnDestroy");*/
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(!isDraws())
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0) {
            setNullToActionMode();
        }
        Log.i(TAG,"onActivityResult");


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile:

                break;
            case R.id.nav_notifications:
                if(isOneClick()) {
                    Intent intent = new Intent(this, NotificationActivity.class);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_logout:
                new MyDialogFragment().show(getSupportFragmentManager(), "tag");
                break;
        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_friednsandgroupandchannelandtournament_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                if(isOneClick()) {
                    Intent intent = new Intent(this, SearchActivity.class);
                    startActivityForResult(intent, 2);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onYesDialog() {
        locationMyActivityManager.createLocationOrUpdate("LoginAndRegistryActivity");
        dbLocalFriends.deleteListFriends();
        MyAccSingle.getInstance(getApplicationContext()).deleteAcc();
    }

    @Override
    public int onMessageDialog() {
        return R.string.nav_logout;
    }



    @Override
    public void deleteRows() {
        SparseBooleanArray selected = friendsAdapter.getSelectedIds();//Get selected ids

        final FriendsAdapter adapter = (FriendsAdapter) friendsRecyclerView.getAdapter();
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                final Friends friendUser = adapter.getItem(selected.keyAt(i));
                myGetFriends.deleteFriends(myAcc.getUid(),friendUser.getUid()).enqueue(new Callback<Friends>() {
                    @Override
                    public void onResponse(Call<Friends> call, Response<Friends> response) {
                        dbLocalFriends.deleteFriends(friendUser.getUid());
                        friendsAdapter.deleteItems(friendUser);
                        Log.i("MainActivity","OKOKss");
                    }

                    @Override
                    public void onFailure(Call<Friends> call, Throwable t) {
                        Log.i("MainActivity","ERRORR" + t.getMessage());
                    }
                });

            }
        }
        Toast.makeText(this, selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        getmActionMode().finish();

    }



    private boolean isOneClick() {
        if(isOneClick) {
            isOneClick = false;
            return true;
        } else {
            return false;
        }
    }

    private boolean isDraws() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Log.d(TAG,"ISDRAWSTRUE");
            return true;
        }
        Log.d(TAG,"ISDRAWS");
        return false;
    }

    private void initialiseCountText() {
        notificationTextView.setGravity(Gravity.CENTER_VERTICAL);
        notificationTextView.setTypeface(null, Typeface.BOLD);
        notificationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        notificationTextView.setText("");
    }


    private void getListFriendsLocal() {
        friendsAdapter.clearItems();
        friendsAdapter.setItems(dbLocalFriends.getListFriends());
        friendsAdapter.setMyId(myAcc.getUid());
    }

    private class MyAsyncGetListFriendsNetwork extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(final Void... voidsa) {
            myGetFriends.getFriends(myAcc.getUid()).enqueue(new Callback<Friends>() {
                @Override
                public void onResponse(Call<Friends> call, Response<Friends> response) {
                    List<Friends> friendList = response.body().getFriendsList();
                    if(friendList == null){
                        friendList = new ArrayList<>();
                    }
                    if(friendList.size() > 0) {
                        Log.d(TAG, String.valueOf(friendList.size()));
                        dbLocalFriends.addListFriends(friendList);
                        getListFriendsLocal();
                    } else {
                        friendsAdapter.clearItems();
                        friendsAdapter.setItems(friendList);
                        friendsAdapter.setMyId(myAcc.getUid());
                    }
                    Log.i("MainActivity","OKOK");
                }

                @Override
                public void onFailure(Call<Friends> call, Throwable t) {
                    Log.e("MainActivity","ERRORR" + t.getMessage());
                }
            });

            return null;
        }
    }

}
