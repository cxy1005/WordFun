package com.example.applicationtest.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyWordManageHelper extends SQLiteOpenHelper {

    private static MyWordManageHelper mInstance;
    private String tableName;

    // 私有构造方法
    private MyWordManageHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, String tableName) {
        super(context, name, factory, version);
        this.tableName = tableName;
    }

    // 获取实例的静态方法，现在需要传入tableName参数
    public static synchronized MyWordManageHelper getInstance(Context context, String tableName) {
        if (mInstance == null || !mInstance.getTableName().equals(tableName)) {
            mInstance = new MyWordManageHelper(context, tableName, null, 1, tableName);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, tableName);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时的处理
    }

    // 动态创建表的方法
    public void createTable(SQLiteDatabase db, String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT, picture TEXT, " +
                "translations TEXT, " +
                "ukphone TEXT, " +
                "ukspeech TEXT, " +
                "usphone TEXT, " +
                "usspeech TEXT, " +
                "type integer, " +
                "learn_time date)";
        db.execSQL(sql);
    }

    // 获取当前表名
    public String getTableName() {
        return tableName;
    }

    // 动态切换表（会创建新表如果不存在）
    public void switchTable(String newTableName) {
        this.tableName = newTableName;
        SQLiteDatabase db = getWritableDatabase();
        createTable(db, newTableName);
        db.close();
    }
}