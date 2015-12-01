package com.jimtang.myshare.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jimtang.myshare.exception.DataRetrievalException;
import com.jimtang.myshare.model.CumulativeCost;
import com.jimtang.myshare.model.MonetaryAmount;
import com.jimtang.myshare.model.MyShareSession;
import com.jimtang.myshare.model.Share;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.jimtang.myshare.db.MyShareTables.SESSIONS;
import static com.jimtang.myshare.db.MyShareTables.SHARES;
import static com.jimtang.myshare.db.MyShareTables.SHARE_PORTIONS;
import static com.jimtang.myshare.db.MyShareTables.SHARE_TOTALS;

/**
 * Created by tangz on 12/1/2015.
 */
public class GetShareDAO {

    private MyShareDbHelper dbHelper;

    public static GetShareDAO getInstanceForContext(Context context) {
        return new GetShareDAO(new MyShareDbHelper(context));
    }

    public GetShareDAO(MyShareDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<MyShareSession> getAllSessions() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            cursor = db.query(SESSIONS.tableName(),
                    new String[]{SESSIONS._ID, MyShareTables.COLUMN_NAME, MyShareTables.COLUMN_SAVED_ON},
                    null,
                    null,
                    null,
                    null,
                    MyShareTables.COLUMN_SAVED_ON + " DESC");

            List<MyShareSession> sessions = Lists.newArrayList();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String sessionName = cursor.getString(cursor.getColumnIndex(MyShareTables.COLUMN_NAME));

                String timeStr = cursor.getString(cursor.getColumnIndex(MyShareTables.COLUMN_SAVED_ON));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateFormat.parse(timeStr);

                Long id = cursor.getLong(cursor.getColumnIndex(SESSIONS._ID));
                List<Share> shares = getAllSharesForSession(id);
                sessions.add(new MyShareSession(sessionName, shares, date));
            }
            return sessions;

        } catch (ParseException e) {
            throw new DataRetrievalException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    List<Share> getAllSharesForSession(Long sessionId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            cursor = db.query(SHARES.tableName(),
                    new String[]{SHARES._ID},
                    MyShareTables.COLUMN_SESSION_ID + "=?",
                    new String[] {sessionId.toString()},
                    null,
                    null,
                    null);

            List<Share> shares = Lists.newArrayList();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Long shareId = cursor.getLong(cursor.getColumnIndex(SHARES._ID));
                Map<String, MonetaryAmount> amts = getPortionsForShare(shareId);
                CumulativeCost totals = getTotalsForShare(shareId);
                String person = getPersonForShare(shareId);
                shares.add(new Share(person, amts, totals));
            }
            return shares;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getPersonForShare(Long shareId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT p.name FROM people AS p WHERE p._id = (SELECT s.person_id FROM shares AS s where s._id = ?)",
                    new String[]{shareId.toString()});

            cursor.moveToFirst();
            return cursor.getString(0);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private CumulativeCost getTotalsForShare(Long shareId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            cursor = db.query(SHARE_TOTALS.tableName(),
                    new String[]{MyShareTables.COLUMN_SUBTOTAL, MyShareTables.COLUMN_TAX, MyShareTables.COLUMN_TIP},
                    MyShareTables.COLUMN_SHARE_ID + "=?",
                    new String[] {shareId.toString()},
                    null,
                    null,
                    null);

            cursor.moveToFirst();

            String subtotalStr = cursor.getString(cursor.getColumnIndex(MyShareTables.COLUMN_SUBTOTAL));
            MonetaryAmount subtotal = new MonetaryAmount(subtotalStr);

            String taxStr = cursor.getString(cursor.getColumnIndex(MyShareTables.COLUMN_TAX));
            MonetaryAmount tax = new MonetaryAmount(taxStr);

            String tipStr = cursor.getString(cursor.getColumnIndex(MyShareTables.COLUMN_TIP));
            MonetaryAmount tip = new MonetaryAmount(tipStr);

            return new CumulativeCost(subtotal, tax, tip);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Map<String, MonetaryAmount> getPortionsForShare(Long shareId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            cursor = db.query(SHARE_PORTIONS.tableName(),
                    new String[]{MyShareTables.COLUMN_EXPENSE_NAME, MyShareTables.COLUMN_AMOUNT},
                    MyShareTables.COLUMN_SHARE_ID + "=?",
                    new String[] {shareId.toString()},
                    null,
                    null,
                    null);

            Map<String, MonetaryAmount> amountMap = Maps.newHashMap();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String expenseName = cursor.getString(cursor.getColumnIndex(MyShareTables.COLUMN_EXPENSE_NAME));

                String amtStr = cursor.getString(cursor.getColumnIndex(MyShareTables.COLUMN_AMOUNT));
                MonetaryAmount amt = new MonetaryAmount(amtStr);

                amountMap.put(expenseName, amt);
            }
            return amountMap;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
