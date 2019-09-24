package org.gamemessenger.Architectur.MVC.model.helper.Manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.gamemessenger.ValidCode.Activity.LoginAndRegistryActivity;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "iDeathPref";



    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String id){
        // Storing login value as TRUE
        editor.putBoolean("isLoggedIn", true);

        // Storing name in pref
        editor.putString("nick", name);

        // Storing email in pref
        editor.putString("id", id);

        // commit changes
        editor.commit();
    }



    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginAndRegistryActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put("nick", pref.getString("nick", null));

        // user email id
        user.put("id", pref.getString("id", null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean("isLoggedIn", false);
    }
}
