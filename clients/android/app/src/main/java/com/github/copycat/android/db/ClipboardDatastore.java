package com.github.copycat.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ClipboardDatastore extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clipboard-history.db";
    public static final String TABLE_NAME = "clipboard_data";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CREATED = "timestamp";

    public ClipboardDatastore(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String[] sCreateQueries = {"CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " LONG PRIMARY KEY UNIQUE, "
                + COLUMN_CONTENT + " TEXT, "
                + COLUMN_CREATED + " LONG)",
                "CREATE INDEX timestamp_idx on " + TABLE_NAME + "(" + COLUMN_CREATED + ")"};
        db.execSQL(sCreateQueries[0]);
        db.execSQL(sCreateQueries[1]);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertEntry(ClipboardModel item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int row_id = item.getContents().hashCode();
        ClipboardModel existingRow = getEntry(row_id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CONTENT, item.getContents());
        contentValues.put(COLUMN_CREATED, item.getCreationDate());
        contentValues.put(COLUMN_ID, row_id);

        if ( existingRow == null ) {
            db.insert(TABLE_NAME, null, contentValues);
        } else {
            db.update(TABLE_NAME, contentValues, COLUMN_ID + " = " + row_id, null);
        }
        return row_id;
    }

    public ClipboardModel getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " where id = " + id, null);
        ClipboardModel result = null;

        if ( res != null && res.moveToFirst() ) {

            int idx_id = res.getColumnIndex(COLUMN_ID);
            int idx_cr = res.getColumnIndex(COLUMN_CREATED);
            int idx_co = res.getColumnIndex(COLUMN_CONTENT);

            long row_id = res.getLong(idx_id);
            long created = res.getLong(idx_cr);
            String content = res.getString(idx_co);
            res.close();
            result = new ClipboardModel(row_id, content, created);
        }

        return result;
    }

    public Integer deleteEntry(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<ClipboardModel> getAllEntries() {
        ArrayList<ClipboardModel> entries = new ArrayList<ClipboardModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT  * FROM " + TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ClipboardModel data = new ClipboardModel(res.getLong(res.getColumnIndex(COLUMN_ID)),
                    res.getString(res.getColumnIndex(COLUMN_CONTENT)),
                    res.getLong(res.getColumnIndex(COLUMN_CREATED)));

            entries.add(data);
            res.moveToNext();
        }
        return entries;
    }
}