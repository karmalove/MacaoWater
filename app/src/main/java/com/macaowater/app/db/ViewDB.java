package com.macaowater.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.macaowater.app.model.ViewConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karma on 2016/4/26.
 * 数据库
 */
public class ViewDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "view_config";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static ViewDB sViewDB;
    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private ViewDB(Context context) {
        ViewOpenHelper viewOpenHelper = new ViewOpenHelper(context, DB_NAME, null, VERSION);
        db = viewOpenHelper.getWritableDatabase();
    }

    /**
     * 获取ViewDB实例
     *
     * @param context
     * @return
     */
    public synchronized static ViewDB getInstance(Context context) {
        if (sViewDB == null) {
            sViewDB = new ViewDB(context);
        }
        return sViewDB;
    }

   /* *//**
     * 保存xy坐标
     * @param view_Id
     * @param view_X
     * @param view_Y
     *//*
    public void saveXY(String view_Id,float view_X,float view_Y){
        ContentValues values=new ContentValues();
        values.put("view_Id",view_Id);
        values.put("view_X",view_X);
        values.put("view_Y",view_Y);
        db.insert("Config",null,values);
    }
    public float getXY(){
        //查询Config表中的所有数据
        Cursor cursor=db.query("Config",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String view_Id=cursor.getString(cursor.getColumnIndex("view_Id"));
                float view_X=cursor.getFloat(cursor.getColumnIndex("view_X"));
                float view_Y=cursor.getFloat(cursor.getColumnIndex("view_Y"));
            }while (cursor.moveToNext());
        }
        return 2.0f;
    }*/

    /**
     * 保存自定义view坐标
     *
     * @param viewConfig
     */
    public void saveViewConfig(ViewConfig viewConfig) {
        ContentValues values = new ContentValues();
        values.put("view_Id", viewConfig.getView_Id());
        values.put("view_X", viewConfig.getView_X());
        values.put("view_Y", viewConfig.getView_Y());
        db.insert("Config", null, values);
    }

    /**
     * 读取自定义view坐标
     * @return
     */
    public List<ViewConfig> getViewConfig() {
        List<ViewConfig> viewConfigs = new ArrayList<ViewConfig>();
        Cursor cursor = db.query("Config", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ViewConfig viewConfig = new ViewConfig();
                viewConfig.setView_Id(cursor.getInt(cursor.getColumnIndex("view_Id")));
                viewConfig.setView_X(cursor.getInt(cursor.getColumnIndex("view_X")));
                viewConfig.setView_Y(cursor.getInt(cursor.getColumnIndex("view_Y")));
                viewConfigs.add(viewConfig);
            } while (cursor.moveToNext());
        }
        return viewConfigs;
    }
}
