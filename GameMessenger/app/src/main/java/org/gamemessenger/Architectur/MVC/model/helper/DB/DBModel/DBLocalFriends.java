package org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import org.gamemessenger.Architectur.MVC.model.Friends;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBSQL;
import org.gamemessenger.Architectur.REST.Singleton.MyAccSingle;

import java.util.ArrayList;
import java.util.List;

public class DBLocalFriends extends DBSQL {

    private static final String TAG = DBLocalFriends.class.getSimpleName();

    private static final int DATABASE_VERSION = 20;

    private static final String DATABASE_NAME = "DBLocalFriends.db";

    private static final String TABLE_NAME_FRIENDS = "friends";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_USER_ID = "user_id";

    private static final String COLUMN_LAST_MESSAGE = "last_message";

    private static final String COLUMN_FRID = "frId";

    private static final String COLUMN_LAST_MESSAGE_TIME = "last_message_time";

    private static final String COLUMN_NOTIFSUB_FRIENDS = "notifSubFriends";

    private static final String COLUMN_NICK = "nick";




    private DBLocalMessageFriends dbLocalMessageFriends;

    private Context context;

    public DBLocalFriends(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context =   context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_FRIENDS = "CREATE  TABLE " + TABLE_NAME_FRIENDS + "("
                                     + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                    +  COLUMN_USER_ID +" INTEGER,"
                                    +  COLUMN_LAST_MESSAGE + " TEXT,"
                                    +  COLUMN_FRID + " TEXT,"
                                    +  COLUMN_LAST_MESSAGE_TIME + " BIGINT,"
                                    +  COLUMN_NOTIFSUB_FRIENDS + " TEXT,"
                                    +  COLUMN_NICK  + " TEXT)";

        db.execSQL(CREATE_TABLE_FRIENDS);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FRIENDS);

        onCreate(db);
    }


    public boolean isEmptyListFriends() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME_FRIENDS;

        SQLiteDatabase db =  this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.getCount() == 0) {
            closeDB(db,cursor);
            return true;
        } else {
            closeDB(db,cursor);
            return false;
        }
    }

    public void addFriends(Friends friends){
        SQLiteDatabase db = this.getWritableDatabase();

        dbLocalMessageFriends = new DBLocalMessageFriends(context);

        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID,friends.getUid());
        values.put(COLUMN_LAST_MESSAGE,friends.getLast_message());
        values.put(COLUMN_FRID,friends.getFrId());
        values.put(COLUMN_LAST_MESSAGE_TIME,friends.getLast_message_time());
        values.put(COLUMN_NOTIFSUB_FRIENDS,friends.getNotifSubFriends());
        values.put(COLUMN_NICK,friends.getNick());
        long dbFr =  db.insert(TABLE_NAME_FRIENDS,null,values);

        closeDB(db,null);

        dbLocalMessageFriends.addTableMessage(friends.getUid());
        Log.d(TAG, "New friends inserted into sqlite: " + dbFr);
    }


    public void addListFriends(List<Friends> list) {
        SQLiteDatabase db = this.getWritableDatabase();

        dbLocalMessageFriends = new DBLocalMessageFriends(context);

        ContentValues values = new ContentValues();
        for (Friends friends : list) {
            values.put(COLUMN_USER_ID,friends.getUid());
            values.put(COLUMN_LAST_MESSAGE,friends.getLast_message());
            values.put(COLUMN_FRID,friends.getFrId());
            values.put(COLUMN_LAST_MESSAGE_TIME,friends.getLast_message_time());
            values.put(COLUMN_NOTIFSUB_FRIENDS,friends.getNotifSubFriends());
            values.put(COLUMN_NICK,friends.getNick());
            long dbFr =  db.insert(TABLE_NAME_FRIENDS,null,values);

            dbLocalMessageFriends.addTableMessage(friends.getUid());
            Log.d(TAG, "New friends inserted into sqlite: " + dbFr);
        }

        closeDB(db,null);
    }

    public void updateFriends(Friends friends) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_LAST_MESSAGE,friends.getLast_message());
        contentValues.put(COLUMN_FRID,friends.getFrId());
        contentValues.put(COLUMN_LAST_MESSAGE_TIME,friends.getLast_message_time());
        contentValues.put(COLUMN_NICK,friends.getNick());
        contentValues.put(COLUMN_NOTIFSUB_FRIENDS,friends.getNotifSubFriends());

        db.update(TABLE_NAME_FRIENDS,contentValues,COLUMN_USER_ID + "=" + friends.getUid(),null);

        closeDB(db,null);
    }


    public List<Friends> getListFriends() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Friends> listFriends = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_FRIENDS,null);

        if(cursor.moveToFirst()) {
            do {
                int user_id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                String last_message = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE));
                String frId = cursor.getString(cursor.getColumnIndex(COLUMN_FRID));
                long last_message_time = cursor.getLong(cursor.getColumnIndex(COLUMN_LAST_MESSAGE_TIME));
                String notifSub = cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFSUB_FRIENDS));
                String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NICK));

                listFriends.add(new Friends(user_id,frId,nick,last_message,last_message_time,notifSub));
            } while (cursor.moveToNext());
        }

        closeDB(db,cursor);

        Log.d(TAG,"list friends local, size: " + listFriends.size());

        return listFriends;
    }

    public Friends getFriends(int users_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_FRIENDS + " WHERE "
                                    + COLUMN_USER_ID + " = " + users_id,null);
        Friends friends = null;

        if(cursor.moveToFirst()) {
            int user_id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            String last_message = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE));
            String frId = cursor.getString(cursor.getColumnIndex(COLUMN_FRID));
            long last_message_time = cursor.getLong(cursor.getColumnIndex(COLUMN_LAST_MESSAGE_TIME));
            String notifSub = cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFSUB_FRIENDS));
            String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NICK));

            friends = new Friends(user_id,frId,nick,last_message,last_message_time,notifSub);
        }

        closeDB(db,cursor);

        return friends;
    }


    public void deleteFriends(int user_id) {
        if(!isEmptyListFriends()) {
            SQLiteDatabase db = this.getWritableDatabase();

            dbLocalMessageFriends = new DBLocalMessageFriends(context);

            String sql = "DELETE FROM " + TABLE_NAME_FRIENDS + " WHERE " + COLUMN_USER_ID + " = " + user_id;

            db.execSQL(sql);

            closeDB(db, null);

            dbLocalMessageFriends.deleteTableMessage(user_id);

        }
    }


    public void deleteListFriends() {
        SQLiteDatabase db = this.getWritableDatabase();

        dbLocalMessageFriends = new DBLocalMessageFriends(context);

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_NAME_FRIENDS,null);

        if(cursor.moveToFirst()) {
            do {
                int user_id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));

                dbLocalMessageFriends.deleteTableMessage(user_id);
            }while (cursor.moveToNext());
        }

        db.delete(TABLE_NAME_FRIENDS,null,null);

        closeDB(db,null);

        Log.d(TAG, "Deleted all friends info from sqlite");
    }

}
