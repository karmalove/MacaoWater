package com.macaowater.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kevin on 2016/4/25.
 */
public class ViewOpenHelper extends SQLiteOpenHelper {
    /**
     * Province表建表语句
     */
    private static final String MAOCO_WATER = "create table Config("
            + "id integer primary key autoincrement,"
            + "view_Id int,"
            + "view_X int,"
            + "view_Y int)";

    public ViewOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MAOCO_WATER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
