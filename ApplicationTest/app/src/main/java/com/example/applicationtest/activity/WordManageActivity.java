package com.example.applicationtest.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtest.R;
import com.example.applicationtest.adapter.MyWordDBRecycleViewAdapter;
import com.example.applicationtest.model.Table;
import com.example.applicationtest.sqlite.MyWordManageHelper;
import com.example.applicationtest.sqlite.SQLiteTableCounter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tencent.mmkv.MMKV;

import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordManageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WordManageActivity";

    private static final int REQUEST_CODE_PERMISSION = 1001;
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(WordManageActivity.class);
    private String tableName;
    public TextView curWordDB;

    private List<Table> list = new ArrayList<>();
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openFilePicker();
                } else {
                    Toast.makeText(this, "需要存储权限才能导入CSV文件", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            handleSelectedFile(result.getData());
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_word_manage);

        Button csvImport = findViewById(R.id.createWordDB);
        csvImport.setOnClickListener(this);

        //准备数据填入词库列表
        SQLiteTableCounter counter = new SQLiteTableCounter(this);
        List<String> databaseNames = counter.getDatabaseNames();
        for (int i = 0; i < databaseNames.size(); i++) {
            Table table = new Table();
            table.setName(databaseNames.get(i));

            list.add(table);
        }

        //RecycleView
        RecyclerView rv = findViewById(R.id.rv);
        //布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        //装载并设置适配器
        MyWordDBRecycleViewAdapter myRecycleViewAdapter = new MyWordDBRecycleViewAdapter(this,list);
        rv.setAdapter(myRecycleViewAdapter);

        curWordDB = findViewById(R.id.curWordDB);
        //获取全局MMKV
        MMKV mmkv = MMKV.defaultMMKV();
        String name = mmkv.decodeString("curWordDB");
        curWordDB.setText(name);

    }
    public void updateCurrentWordDB(String wordDBName) {
        curWordDB.setText(wordDBName);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createWordDB){
            checkAndRequestPermission();
        }
    }

    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");  // 更宽松的文件类型过滤
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"text/csv", "text/comma-separated-values"});
        filePickerLauncher.launch(intent);
    }

    private void handleSelectedFile(Intent data) {
        Uri uri = data.getData();
        if (uri == null) {
            Log.e(TAG, "Selected file URI is null");
            Toast.makeText(this, "无法获取文件URI", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        try {
            // 获取文件名
            String fileName = getFileName(uri);
            Log.d(TAG, "正在处理文件: " + fileName);

            // 获取永久读取权限
            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // 从文件名获取表名（去掉.csv后缀）
            tableName = fileName.substring(0, fileName.lastIndexOf('.'));
            Log.d(TAG, "将数据导入表: " + tableName);

            // 获取数据库帮助类实例
            MyWordManageHelper helper = MyWordManageHelper.getInstance(this, tableName);
            db = helper.getWritableDatabase();

            // 开始事务，提高批量插入性能
            db.beginTransaction();

            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                throw new IOException("无法打开文件输入流");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVReader csvReader = new CSVReader(reader);

            String[] nextLine;
            int insertedRows = 0;

            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length >= 2) {  // 确保至少有2列
                    String word = nextLine[0].trim();
                    String picture = nextLine[1].trim();
                    Log.d(TAG, "导入数据 - word: " + word + ", picture: " + picture);

                    // 插入数据到数据库
                    ContentValues values = new ContentValues();
                    values.put("word", word);
                    values.put("picture", picture);

                    long rowId = db.insert(tableName, null, values);
                    if (rowId != -1) {
                        insertedRows++;
                    } else {
                        Log.e(TAG, "插入数据失败: " + word);
                    }
                }
            }

            // 标记事务成功完成
            db.setTransactionSuccessful();

            csvReader.close();
            Toast.makeText(this,
                    "成功导入 " + insertedRows + " 条数据到表 " + tableName,
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(TAG, "文件读取错误", e);
            Toast.makeText(this, "文件读取错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (CsvValidationException e) {
            Log.e(TAG, "CSV格式错误", e);
            Toast.makeText(this, "CSV文件格式错误", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "未知错误", e);
            Toast.makeText(this, "导入失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // 结束事务
            if (db != null && db.inTransaction()) {
                db.endTransaction();
            }
            // 关闭数据库连接
            if (db != null) {
                db.close();
            }
        }
    }

    // 从URI获取文件名的方法
    private String getFileName(Uri uri) {
        String displayName = "文件名";
        try (Cursor cursor = getContentResolver().query(
                uri,
                new String[]{android.provider.OpenableColumns.DISPLAY_NAME},
                null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    displayName = cursor.getString(nameIndex);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "获取文件名失败", e);
        }
        return displayName;
    }
}