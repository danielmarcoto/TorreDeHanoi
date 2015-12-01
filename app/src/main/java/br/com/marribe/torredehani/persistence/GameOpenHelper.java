package br.com.marribe.torredehani.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by danielmarcoto on 01/12/15.
 */
public class GameOpenHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "game.db";
    private static int DATABASE_VERSION = 1;

    public GameOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.GameEntry.CREATE_TABLE_SCRIPT);
        db.execSQL(DbContract.MovementEntry.CREATE_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.GameEntry.DROP_TABLE_SCRIPT);
        db.execSQL(DbContract.MovementEntry.DROP_TABLE_SCRIPT);

        this.onCreate(db);
    }
}
