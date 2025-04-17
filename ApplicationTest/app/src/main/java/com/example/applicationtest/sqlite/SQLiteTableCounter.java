package com.example.applicationtest.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteTableCounter {
    private Context context;

    public SQLiteTableCounter(Context context) {
        this.context = context;
    }

    public List<String> getDatabaseNames() {
        List<String> databaseNames = new ArrayList<>();
        String[] databases = context.databaseList();
        for (String dbName : databases) {
            databaseNames.add(dbName);
        }
        return databaseNames;
    }

    public int getTableCount(String databaseName) {
        SQLiteOpenHelper helper = new SQLiteOpenHelper(context, databaseName, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                // 不需要实现
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // 不需要实现
            }
        };
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        int tableCount = cursor.getCount();
        cursor.close();
        db.close();
        return tableCount;
    }
}
