package com.jimtang.myshare.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ListIterator;

/**
 * Created by tangz on 11/8/2015.
 */
public class MyShareDbHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public static final String DB_NAME = "com.jimtang.MyShare.db";

    public MyShareDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (TableDefinition table: MyShareTables.ALL) {
            db.execSQL(table.createStatement());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: fix default implementation of dropping all tables. We want to persist the data.
        TableDefinition table = null;
        for (ListIterator<TableDefinition> itr = MyShareTables.ALL.listIterator(MyShareTables.ALL.size());
             itr.hasPrevious();
             table = itr.previous()) {

            if (table != null) {
                db.execSQL(table.dropStatement());
            }
        }
        onCreate(db);
    }
}
