package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * author: zhanghuabin
 * date: 2018/6/29 15:19
 * github: https://github.com/zhanghuabin-sh
 * email: 2908882095@qq.com
 * 1、创建 PetDbHelper 继承自 SQLiteOpenHelper
 * 2、重写 onCreate() 和 onUpgrade() 方法
 * 3、为数据库名称和版本创建常量
 * 4、创建该类的构造函数
 * 5、为用来创建表格的 SQLite 指令创建一个字符串常量
 */
public class PetDbHelper extends SQLiteOpenHelper{


    private static final  String DATABASE_NAME = "shelter.db";

    private static final int DATABASE_VERSION = 1;

    // 使用 SQL 语句来创建和初始化架构
    private static final String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
            + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
            + PetEntry.COLUMN_PET_BREED + " TEXT , "
            + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
            + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         *
         * db.execSQL()方法：
         * 该方法不得与任何 SELECT 语句一起使用，因为该方法不会返回汾河实际的数据，只是用来执行语句
         * ，从而修改数据库的配置和结构
         *
         * */
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /**
     *
     * 该方法会删除数据库表格并重新创建，其中执行的 SQL 语句
     *
     * 根据你对代码结构的更改，更新数据库文件。假设新版本应用更新了表格，我们就可以使数据库版本加1，这个更新的消息
     * 被传递给了 SQLiteOpenHelper，然后 onUpgrade() 使你能够执行额外的 SQL 语句，从而修改数据库文件。
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
