package com.jimtang.myshare.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.collect.Lists;
import com.jimtang.myshare.model.CumulativeCost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;
import com.jimtang.myshare.model.MyShareSession;
import com.jimtang.myshare.model.Share;

import java.util.List;
import java.util.Map;

import static com.jimtang.myshare.db.MyShareTables.COLUMN_NAME;
import static com.jimtang.myshare.db.MyShareTables.PEOPLE;
import static com.jimtang.myshare.db.MyShareTables.SESSIONS;
import static com.jimtang.myshare.db.MyShareTables.SHARES;

/**
 * Created by tangz on 11/10/2015.
 */
public class MyShareDbDAO {

    private MyShareDbHelper dbHelper;

    public static MyShareDbDAO getInstanceForContext(Context context) {
        return new MyShareDbDAO(new MyShareDbHelper(context));
    }

    public MyShareDbDAO(MyShareDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    Long savePerson(String person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, person);

        return db.insert(PEOPLE.tableName(), null, values);
    }

    Long getLastPersonId(String person) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(PEOPLE.tableName(),
                new String[] {String.format("MAX(%s) as %s", PEOPLE._ID, PEOPLE._ID)},
                COLUMN_NAME + "=?",
                new String[]{person},
                null,
                null,
                null);

        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndexOrThrow(PEOPLE._ID));
    }

    public Long saveSession(MyShareSession session) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (String person: session.getPeople()) {
            savePerson(person);
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, session.getSessionName());

        Long sessionId = db.insert(SESSIONS.tableName(), null, values);
        for (Share share: session.getShares()) {
            saveShare(sessionId, share);
        }
        return sessionId;
    }

    Long saveShare(Long sessionId, Share share) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Long maxPersonId = getLastPersonId(share.getPersonName());
        ContentValues values = new ContentValues();
        values.put(MyShareTables.COLUMN_PERSON_ID, maxPersonId);
        values.put(MyShareTables.COLUMN_SESSION_ID, sessionId);

        Long shareId = db.insert(SHARES.tableName(), null, values);
        saveSharePortions(shareId, share.getExpensePortions());
        saveShareTotals(shareId, share.getIndivCumulativeCost());
        return shareId;
    }

    private List<Long> saveSharePortions(Long shareId, Map<String, MonetaryAmount> portions) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final List<Long> ids = Lists.newArrayList();
        for (Map.Entry<String, MonetaryAmount> entry: portions.entrySet()) {
            ContentValues values = new ContentValues();
            values.put(MyShareTables.COLUMN_SHARE_ID, shareId);
            values.put(MyShareTables.COLUMN_EXPENSE_NAME, entry.getKey());
            values.put(MyShareTables.COLUMN_AMOUNT, entry.getValue().toDouble());
            Long sharePortionId = db.insert(MyShareTables.SHARE_PORTIONS.tableName(), null, values);
            ids.add(sharePortionId);
        }

        return ids;
    }

    private Long saveShareTotals(Long shareId, CumulativeCost totals) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyShareTables.COLUMN_SHARE_ID, shareId);
        values.put(MyShareTables.COLUMN_SUBTOTAL, totals.getSubtotal().toBigDecimal().doubleValue());
        values.put(MyShareTables.COLUMN_TAX, totals.getTax().toBigDecimal().doubleValue());
        values.put(MyShareTables.COLUMN_TIP, totals.getTip().toBigDecimal().doubleValue());

        return db.insert(MyShareTables.SHARE_TOTALS.tableName(), null, values);
    }
}
