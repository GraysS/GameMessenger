package org.gamemessenger.Architectur.MVC.model.helper.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public abstract class DBSQL extends SQLiteOpenHelper {

    public DBSQL(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    protected void closeDB(SQLiteDatabase db, Cursor cursor) {

        try {
            if (cursor != null) {
                cursor.close();
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        db.close();
    }
}
