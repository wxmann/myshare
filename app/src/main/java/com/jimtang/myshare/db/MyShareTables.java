package com.jimtang.myshare.db;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Created by tangz on 11/8/2015.
 */
public final class MyShareTables {

    static final String PEOPLE_TABLE_NAME = "PEOPLE";
//    static final String EXPENSES_TABLE_NAME = "EXPENSES";
    static final String SESSIONS_TABLE_NAME = "SESSIONS";
    static final String SHARES_TABLE_NAME = "SHARES";
    static final String SHARE_TOTALS_TABLE_NAME = "SHARE_TOTALS";
    static final String SHARE_PORTIONS_TABLE_NAME = "SHARE_PORTIONS";

    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_PERSON_ID = "PERSON_ID";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_EXPENSE_NAME = "EXPENSE_NAME";
    public static final String COLUMN_SAVED_ON = "SAVED_ON";
    public static final String COLUMN_SESSION_ID = "SESSION_ID";
    public static final String COLUMN_SHARE_ID = "SHARE_ID";
//    public static final String COLUMN_EXPENSE_ID = "EXPENSE_ID";
    public static final String COLUMN_SUBTOTAL = "SUBTOTAL";
    public static final String COLUMN_TAX = "TAX";
    public static final String COLUMN_TIP = "TIP";

    private MyShareTables() {
    }

    private abstract static class AbstractSQLiteTableDefinition implements TableDefinition {
        @Override
        public String dropStatement() {
            return "DROP TABLE IF EXISTS " + tableName();
        }
    }

    public static final TableDefinition PEOPLE = new AbstractSQLiteTableDefinition() {

        @Override
        public String tableName() {
            return PEOPLE_TABLE_NAME;
        }

        @Override
        public String createStatement() {
            StringBuilder builder = new StringBuilder("CREATE TABLE ");
            builder.append(tableName())
                    .append(" (")
                    .append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(COLUMN_NAME).append(" TEXT NOT NULL")
                    .append(");");
            return builder.toString();
        }
    };

//    public static final TableDefinition EXPENSES = new AbstractSQLiteTableDefinition() {
//
//        @Override
//        public String tableName() {
//            return EXPENSES_TABLE_NAME;
//        }
//
//        @Override
//        public String createStatement() {
//            StringBuilder builder = new StringBuilder("CREATE TABLE ");
//            builder.append(tableName())
//                    .append(" (")
//                    .append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
//                    .append(COLUMN_PERSON_ID).append(" INTEGER NOT NULL, ")
//                    .append(COLUMN_AMOUNT).append(" REAL NOT NULL, ")
//                    .append(COLUMN_EXPENSE_NAME).append(" TEXT NOT NULL, ")
//                    .append(String.format("FOREIGN KEY(%s) REFERENCES %s(%s)",
//                            COLUMN_PERSON_ID, PEOPLE_TABLE_NAME, PEOPLE._ID))
//                    .append(");");
//            return builder.toString();
//        }
//    };

    public static final TableDefinition SESSIONS = new AbstractSQLiteTableDefinition() {

        @Override
        public String tableName() {
            return SESSIONS_TABLE_NAME;
        }

        @Override
        public String createStatement() {
            StringBuilder builder = new StringBuilder("CREATE TABLE ");
            builder.append(tableName())
                    .append(" (")
                    .append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(COLUMN_NAME).append(" TEXT NOT NULL, ")
                    .append(COLUMN_SAVED_ON).append(" DATETIME DEFAULT CURRENT_TIMESTAMP")
                    .append(");");
            return builder.toString();
        }
    };

    public static final TableDefinition SHARES = new AbstractSQLiteTableDefinition() {

        @Override
        public String tableName() {
            return SHARES_TABLE_NAME;
        }

        @Override
        public String createStatement() {
            StringBuilder builder = new StringBuilder("CREATE TABLE ");
            builder.append(tableName())
                    .append(" (")
                    .append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(COLUMN_PERSON_ID).append(" TEXT NOT NULL, ")
                    .append(COLUMN_SESSION_ID).append(" INTEGER NOT NULL, ")
                    .append(String.format("FOREIGN KEY(%s) REFERENCES %s(%s), ",
                            COLUMN_PERSON_ID, PEOPLE_TABLE_NAME, PEOPLE._ID))
                    .append(String.format("FOREIGN KEY(%s) REFERENCES %s(%s)",
                            COLUMN_SESSION_ID, SESSIONS_TABLE_NAME, SESSIONS._ID))
                    .append(");");
            return builder.toString();
        }
    };

    public static final TableDefinition SHARE_PORTIONS = new AbstractSQLiteTableDefinition() {

        @Override
        public String tableName() {
            return SHARE_PORTIONS_TABLE_NAME;
        }

        @Override
        public String createStatement() {
            StringBuilder builder = new StringBuilder("CREATE TABLE ");
            builder.append(tableName())
                    .append(" (")
                    .append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(COLUMN_SHARE_ID).append(" INTEGER NOT NULL, ")
                    .append(COLUMN_EXPENSE_NAME).append(" TEXT NOT NULL, ")
                    .append(COLUMN_AMOUNT).append(" REAL NOT NULL, ")
                    .append(String.format("FOREIGN KEY(%s) REFERENCES %s(%s)",
                            COLUMN_SHARE_ID, SHARES_TABLE_NAME, SHARES._ID))
                    .append(");");
            return builder.toString();
        }
    };

    public static final TableDefinition SHARE_TOTALS = new AbstractSQLiteTableDefinition() {

        @Override
        public String tableName() {
            return SHARE_TOTALS_TABLE_NAME;
        }

        @Override
        public String createStatement() {
            StringBuilder builder = new StringBuilder("CREATE TABLE ");
            builder.append(tableName())
                    .append(" (")
                    .append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(COLUMN_SHARE_ID).append(" INTEGER NOT NULL, ")
                    .append(COLUMN_SUBTOTAL).append(" REAL NOT NULL, ")
                    .append(COLUMN_TAX).append(" REAL NOT NULL, ")
                    .append(COLUMN_TIP).append(" REAL NOT NULL, ")
                    .append(String.format("FOREIGN KEY(%s) REFERENCES %s(%s)",
                            COLUMN_SHARE_ID, SHARES_TABLE_NAME, SHARES._ID))
                    .append(");");
            return builder.toString();
        }
    };

    public static final List<TableDefinition> ALL =
            Collections.unmodifiableList(
                    Lists.newArrayList(PEOPLE, SESSIONS, SHARES, SHARE_TOTALS, SHARE_PORTIONS));
}
