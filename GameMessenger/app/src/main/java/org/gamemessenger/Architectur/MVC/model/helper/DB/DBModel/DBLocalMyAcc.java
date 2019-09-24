package org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.gamemessenger.Architectur.MVC.model.MyAcc;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBSQL;
import org.gamemessenger.ValidCode.Activity.LoginAndRegistryActivity;

public class DBLocalMyAcc extends DBSQL {

    private static final String TAG = DBLocalMyAcc.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DBLocalMyAcc.db";

    private static final String TABLE_NAME = "user";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_NICK = "nick";

    private static final String COLUMN_TOKEN = "token";

    private Context context;

    public DBLocalMyAcc(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context =   context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER,"
                               + COLUMN_NICK + " TEXT," + COLUMN_TOKEN + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dbUpdateDowngrade(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dbUpdateDowngrade(db);
    }

    private void dbUpdateDowngrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    public void addMyAccDB(MyAcc myAcc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,myAcc.getUid());
        values.put(COLUMN_NICK,myAcc.getNick());
        values.put(COLUMN_TOKEN,myAcc.getFirebase_token());

        long dbId  = db.insert(TABLE_NAME,null,values);

        closeDB(db,null);

        Log.d(TAG, "New user inserted into sqlite: " + dbId);
    }

    public MyAcc getMyAccDB() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db =  this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        MyAcc myAcc = null;
        if(cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NICK));
            String token = cursor.getString(cursor.getColumnIndex(COLUMN_TOKEN));
            myAcc = new MyAcc(id,nick,token);
        }
        closeDB(db,cursor);

        Log.d(TAG, "Fetching user from Sqlite: " + myAcc.toString());

        return myAcc;
    }

    public boolean isMyAccDB(){
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db =  this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.getCount() == 0) {
            closeDB(db,cursor);
            goIntent();
            return true;
        } else {
            closeDB(db,cursor);
            return false;
        }
    }


    public void deleteMyAccDB() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,null,null);

        closeDB(db,null);

        Log.d(TAG, "Deleted myAcc info from sqlite");

        goIntent();
    }



    private void goIntent() {
        Log.d(TAG, "INTENTSS");
        Intent intent = new Intent(context, LoginAndRegistryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }





}
