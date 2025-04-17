package com.example.applicationtest.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * MySqliteOpenHelper工具类  单例模式
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper {

    //2、对外暴露提供函数
    private static SQLiteOpenHelper mInstance;
    public static synchronized SQLiteOpenHelper getmInstance(Context context){
        if (mInstance == null){
            mInstance = new MySqliteOpenHelper(context,"t_user",null,1);
        }
        return mInstance;
    }

    //1、构造函数私有化
    public MySqliteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(_id integer primary key autoincrement , name text ,password text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
