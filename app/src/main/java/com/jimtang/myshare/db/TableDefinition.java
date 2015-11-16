package com.jimtang.myshare.db;

import android.provider.BaseColumns;

/**
 * Created by tangz on 11/8/2015.
 */
public interface TableDefinition extends BaseColumns {

    String tableName();

    String createStatement();

    String dropStatement();
    
}
