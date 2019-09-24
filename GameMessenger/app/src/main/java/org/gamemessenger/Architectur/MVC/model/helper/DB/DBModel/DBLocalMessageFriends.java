package org.gamemessenger.Architectur.MVC.model.helper.DB.DBModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.gamemessenger.Architectur.MVC.model.MessageFriends;
import org.gamemessenger.Architectur.MVC.model.helper.DB.DBSQL;

import java.util.ArrayList;
import java.util.List;

public class DBLocalMessageFriends extends DBSQL {

    private static final String TAG = DBLocalMessageFriends.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DBLocalMessageFriends.db";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_FRID = "frId";

    private static final String COLUMN_MESSAGE = "message";

    private static final String COLUMN_MESSAGE_TIME = "message_time";

    public DBLocalMessageFriends(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isEmptyListMessage(int user_id) {
        String message_table = "message_" + user_id;

        String selectQuery = "SELECT * FROM " + message_table;

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


    public void addTableMessage(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String message_table = "message_" + user_id;
        String CREATE_TABLE_MESSAGE = "CREATE TABLE  " + message_table + "("
                + COLUMN_ID + " INTEGER,"
                + COLUMN_FRID + " TEXT,"
                + COLUMN_MESSAGE + " TEXT,"
                + COLUMN_MESSAGE_TIME + " BIGINT)";

        db.execSQL(CREATE_TABLE_MESSAGE);

        closeDB(db,null);
    }

    public void addListMessage(List<MessageFriends> list,int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        for (MessageFriends messageFriends : list) {
            values.put(COLUMN_ID, messageFriends.getId());
            values.put(COLUMN_FRID, messageFriends.getFrId());
            values.put(COLUMN_MESSAGE, messageFriends.getMessage());
            values.put(COLUMN_MESSAGE_TIME, messageFriends.getMessage_time());

            String message_table = "message_" + user_id;
            long dbmessage = db.insert(message_table, null, values);

            Log.d(TAG, "New message inserted into sqlite: " + dbmessage);
        }

        closeDB(db,null);
    }


    public void addMessage(MessageFriends messageFriends,int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values  = new ContentValues();

        values.put(COLUMN_ID, messageFriends.getId());
        values.put(COLUMN_FRID, messageFriends.getFrId());
        values.put(COLUMN_MESSAGE, messageFriends.getMessage());
        values.put(COLUMN_MESSAGE_TIME, messageFriends.getMessage_time());

        String message_table = "message_" + user_id;
        long dbmessage = db.insert(message_table, null, values);

        closeDB(db,null);
        Log.d(TAG, "New message inserted into sqlite: " + dbmessage);
    }


    public List<MessageFriends> getListFriends(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<MessageFriends> listFriends = new ArrayList<>();

        String message_table = "message_" + user_id;

        Cursor cursor = db.rawQuery("SELECT * FROM " + message_table,null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String frId = cursor.getString(cursor.getColumnIndex(COLUMN_FRID));
                String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
                long message_time = cursor.getLong(cursor.getColumnIndex(COLUMN_MESSAGE_TIME));

                listFriends.add(new MessageFriends(id,frId,message,message_time));
            } while (cursor.moveToNext());
        }

        closeDB(db,cursor);

        Log.d(TAG,"list message local, size: " + listFriends.size());

        return listFriends;
    }

    public void deleteMessage(int user_id,int id) {
        if(!isEmptyListMessage(user_id)) {
            SQLiteDatabase db = this.getWritableDatabase();

            String message_table = "message_" + user_id;
            String sql = "DELETE FROM " + message_table + " WHERE " + COLUMN_ID + " = " + id;

            db.execSQL(sql);

            closeDB(db, null);
        }
    }


    public void deleteListMessage(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String message_table = "message_" + user_id;

        db.delete(message_table,null,null);

        closeDB(db,null);

        Log.d(TAG, "Deleted all message info from sqlite");
    }

    public void deleteTableMessage(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String message_table = "message_" + user_id;

        String sql = "DROP TABLE IF EXISTS " + message_table;

        db.execSQL(sql);

        closeDB(db,null);

        Log.d(TAG,"Drop table " + user_id);
    }

}
