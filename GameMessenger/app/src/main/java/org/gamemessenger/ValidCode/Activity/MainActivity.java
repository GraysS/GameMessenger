package org.gamemessenger.ValidCode.Activity;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.gamemessenger.Architectur.REST.Singleton.MyAccSingle;
import org.gamemessenger.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!MyAccSingle.getInstance(getApplicationContext()).isAcc()) {
            goIntents(FriendsAndGroupAndChannelAndTournamentActivity.class);
        } else {
            goIntents(LoginAndRegistryActivity.class);
        }
    }


    private void goIntents(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
      //  overridePendingTransition(R.anim.activity_open_enter,R.anim.activity_open_exit);
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
       // overridePendingTransition(R.anim.activity_close_enter,R.anim.activity_close_exit);
    }

}
