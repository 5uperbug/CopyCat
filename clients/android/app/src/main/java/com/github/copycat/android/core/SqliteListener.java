package com.github.copycat.android.core;

import android.content.Context;
import android.util.Log;

import com.github.copycat.android.db.ClipboardDatastore;
import com.github.copycat.android.db.ClipboardModel;

import java.util.Observable;
import java.util.Observer;

public class SqliteListener {

    private final static String TAG = SqliteListener.class.getCanonicalName();

    private static ClipboardDatastore datastore = null;

    public SqliteListener(Context context) {
        datastore = new ClipboardDatastore(context);
    }

    public void update(String data) {
        ClipboardModel clipData = new ClipboardModel(data, System.currentTimeMillis());
        datastore.insertEntry(clipData);
        Log.d(TAG, "Inserted entry into DB: " + data);
    }

}
