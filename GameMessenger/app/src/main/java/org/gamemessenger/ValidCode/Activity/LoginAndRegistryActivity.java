package org.gamemessenger.ValidCode.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.gamemessenger.Architectur.MVC.model.MyAcc;
import org.gamemessenger.Architectur.MVC.model.helper.Manager.TokenManager;
import org.gamemessenger.Architectur.REST.I.IMyAcc;
import org.gamemessenger.Architectur.REST.Singleton.Client;
import org.gamemessenger.Architectur.REST.Singleton.MyAccSingle;
import org.gamemessenger.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAndRegistryActivity extends AppCompatActivity {
    private Button btn_login;
    private LinearLayout ll_loginAndRegistry;
    private LinearLayout ll_login;
    private EditText et_nickname_login;
    private EditText et_password_login;
    private TextView tv_titleToolbar;
    private ProgressBar progressResponse;

    private IMyAcc api;
    private MyAcc myAcc;
    private TokenManager tokenManager;
    private ActionBar actionBar;
    private Menu myMenu;

    private static final String TAG = LoginAndRegistryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_loginAndRegistry);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        btn_login = (Button) findViewById(R.id.btn_login);
        ll_loginAndRegistry = (LinearLayout) findViewById(R.id.ll_loginAndRegistry);

        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        et_nickname_login = (EditText) findViewById(R.id.et_nickname_login);
        et_password_login = (EditText) findViewById(R.id.et_password_login);
        tv_titleToolbar = (TextView) findViewById(R.id.tv_titleToolbar);
        progressResponse = (ProgressBar) findViewById(R.id.progress_response);

        tokenManager = new TokenManager(getApplicationContext());

        api = Client.getIMyAcc();
        // Переход до логина
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_loginAndRegistry.setVisibility(View.GONE);
                ll_login.setVisibility(View.VISIBLE);
                visibleHome();
                tv_titleToolbar.setText(getString(R.string.login));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_loginandregistry_toolbar,menu);
        myMenu = menu;
        invisibleHome();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                inputClose();
                invisibleHome();
                ll_loginAndRegistry.setVisibility(View.VISIBLE);
                if(ll_login.getVisibility() == View.VISIBLE) {
                    ll_login.setVisibility(View.GONE);
                }

                break;
            case R.id.action_done:
                inputClose();

                final String nickname = et_nickname_login.getText().toString().trim();
                final String password = et_password_login.getText().toString().trim();

                if (!TextUtils.isEmpty(nickname) && !TextUtils.isEmpty(password)) {
                    invisibleLoginAndRegistr(item);
                    loginAndRegistr(nickname,password,item);
                } else {
                    showError(R.string.empty);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       Log.d(TAG,"No Work Back Pressed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        inputClose();
    }

    private void goIntents(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
      //  overridePendingTransition(R.anim.activity_open_enter,R.anim.activity_open_exit);
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    public void invisibleLoginAndRegistr(MenuItem item) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        item.setVisible(false);
        progressResponse.setVisibility(View.VISIBLE);
    }

    private void visibleLoginAndRegistr(MenuItem item){
        progressResponse.setVisibility(View.GONE);
        item.setVisible(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void invisibleHome() {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            tv_titleToolbar.setText(getString(R.string.app_name));
        }
        myMenu.findItem(R.id.action_done).setVisible(false);
    }
    private void visibleHome() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        myMenu.findItem(R.id.action_done).setVisible(true);
    }

    private void showError(int id){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fl_loginAndRegistry),id,Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.Okay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        snackbar.show();
    }

    private void inputClose() {
        InputMethodManager inputMethodManager
                = (InputMethodManager) LoginAndRegistryActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()) {
            try {
                inputMethodManager.hideSoftInputFromWindow(
                        LoginAndRegistryActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }catch (NullPointerException e){
                Log.i("LoginAndRegistry","IDI NAH NPE");
            }
        }
    }


    private void loginAndRegistr(final String nickname, final String password, final MenuItem item) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ll_login.getVisibility() == View.VISIBLE) {
                   final MyAsyncGetToken myAsyncGetToken = new MyAsyncGetToken();

                   myAsyncGetToken.execute();

                   Handler handler2 = new Handler();
                   handler2.postDelayed(new Runnable() {
                       @Override
                       public void run() {

                           if(tokenManager.getTokenM() != null) {
                               Log.d("TOKENIZER",tokenManager.getTokenM());
                           }

                           api.login(nickname,password,tokenManager.getTokenM())
                                   .enqueue(new Callback<MyAcc>() {
                                       @Override
                                       public void onResponse(Call<MyAcc> call, Response<MyAcc> response) {
                                           if (!response.body().isError()) {

                                               final String uid = response.body().getUid();
                                               final String nick = response.body().getNick();
                                               final String firebase_token = response.body().getFirebase_token();

                                               myAcc = new MyAcc(uid, nick, firebase_token);

                                               MyAccSingle.getInstance(getApplicationContext()).addAcc(myAcc);
                                                    //  db.addMyAccDB(myAcc);

                                               Log.i("LoginAndRegistry", myAcc.getUid() + " : " + myAcc.getNick() + " : " + myAcc.getFirebase_token());

                                               goIntents(FriendsAndGroupAndChannelAndTournamentActivity.class);
                                           } else {
                                               showError(R.string.incorrect);
                                           }
                                           visibleLoginAndRegistr(item);
                                       }

                                       @Override
                                       public void onFailure(Call<MyAcc> call, Throwable t) {
                                           Log.e("LoginAndRegistry", "Unable to submit post to API. " + t.getMessage());
                                           showError(R.string.connect);
                                           visibleLoginAndRegistr(item);
                                       }
                                   });
                        }
                   },3000);
                } else {

                }
              }
            },3000);
    }

    private class MyAsyncGetToken extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if(!task.isSuccessful()){
                        Log.d(TAG,"NO CONNECTION FIREBASE");
                        return;
                    }
                    String token = task.getResult().getToken();
                    Log.d("First", token);
                    if (tokenManager.isTokenM()) {
                        new MyAsyncTokenDelete().execute();
                        new MyAsynTokenRefresh().execute();
                    } else {
                        tokenManager.createToken(token);
                        Log.d(TAG,"Old TOKEN: " + token);
                    }
                }
            });
            return null;
        }
    }

    private class MyAsynTokenRefresh extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if(!task.isSuccessful()){
                        Log.d(TAG,"NO CONNECTION FIREBASE REFRESH");
                        return;
                    }

                    String token = task.getResult().getToken();
                    tokenManager.createToken(token);
                    Log.d(TAG,"New TOKEN: " + token);
                }
            });
            return null;
        }
    }


    private class MyAsyncTokenDelete extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
