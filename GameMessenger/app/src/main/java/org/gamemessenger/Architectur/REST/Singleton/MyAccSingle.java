package org.gamemessenger.Architectur.REST.Singleton;

import android.content.Context;
import android.util.Log;

import org.gamemessenger.Architectur.MVC.model.MyAcc;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel.DBLocalMyAcc;

public class MyAccSingle
{
    private static MyAccSingle ourInstance;
   // private Context context;

    private DBLocalMyAcc dbLocalMyAcc;

    private MyAccSingle(Context context) {

        Log.i("MyAccSingle","AccGo");
        dbLocalMyAcc = new DBLocalMyAcc(context);
    }

    public static synchronized MyAccSingle getInstance(Context context) {
        if(ourInstance == null) {
            ourInstance = new MyAccSingle(context);
        }
        return ourInstance;
    }

    public MyAcc getAcc() {
        if(!isAcc()) {
            return dbLocalMyAcc.getMyAccDB();
        } else {
            return null;
        }
    }

    public void addAcc(MyAcc myAcc) {
        dbLocalMyAcc.addMyAccDB(myAcc);
    }

    public boolean isAcc() {
        return dbLocalMyAcc.isMyAccDB();
    }

    public void deleteAcc() {
        dbLocalMyAcc.deleteMyAccDB();
    }



}
