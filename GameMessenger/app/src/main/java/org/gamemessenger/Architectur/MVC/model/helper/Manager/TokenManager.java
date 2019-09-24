package org.gamemessenger.Architectur.MVC.model.helper.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {

    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    private static final String TOKEN_M = "TOKEN_M";

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "TokenManager";

    // Constructor
    public TokenManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createToken(String token) {
        // INSERT
        editor.putString(TOKEN_M,token);
        editor.apply();
    }

    public boolean isTokenM() {
        if(pref.contains(TOKEN_M)) {
            if(pref.getString(TOKEN_M,null) != null) {
                Log.i("TokenManager", "OLD TOKEN: " + pref.getString(TOKEN_M, null));
            }
            editor.clear();
            editor.apply();
            Log.i("TokenManager","Token remove");
            return true;
        } else {
            if(pref.getString(TOKEN_M,null) != null) {
                Log.i("TokenManager", pref.getString(TOKEN_M, null) + " NO EQUALS");
            }
            return false;
        }
    }

    public String getTokenM() {
        return pref.getString(TOKEN_M,null);
    }



}
