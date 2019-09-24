package org.gamemessenger.Architectur.MVC.model.helper.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LocationMyActivityManager {
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String LOCATION_M = "LOCATION_M";

    private static final String PREF_NAME = "LocationMyActivityManager";

    private static final String TAG = LocationMyActivityManager.class.getSimpleName();

    // Constructor
    public LocationMyActivityManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLocationOrUpdate(String location) {
        if(pref.contains(LOCATION_M)) {
            editor.clear();
            editor.putString(LOCATION_M, location);
            editor.apply();
            Log.d(TAG,"UPDATE");
        } else {
            editor.putString(LOCATION_M, location);
            editor.apply();
            Log.d(TAG,"INSERT");
        }
    }

    public boolean isLocation() {
        if(pref.getString(LOCATION_M,"LoginAndRegistryActivity").equals("LoginAndRegistryActivity")) {
            Log.d(TAG,"DELETES");
            return true;
        } else {
            return false;
        }
    }

}
