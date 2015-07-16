package me.federicomaggi.suggestme.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import me.federicomaggi.suggestme.controller.DatabaseContract.QuestionTable;
import me.federicomaggi.suggestme.controller.DatabaseContract.SuggestTable;

/**
 * Created by federicomaggi on 24/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "SuggestMe.db";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QuestionTable.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(SuggestTable.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        sqLiteDatabase.execSQL(QuestionTable.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(SuggestTable.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase,oldVersion,newVersion);
    }

    public static int getVersion() {
        return DATABASE_VERSION;
    }
}
