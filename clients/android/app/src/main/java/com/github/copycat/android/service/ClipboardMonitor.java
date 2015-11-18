package com.github.copycat.android.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.github.copycat.android.core.SettingsManager;
import com.github.copycat.android.core.SqliteListener;

public class ClipboardMonitor extends IntentService {

    private final static String TAG = ClipboardMonitor.class.getCanonicalName();

    private static final String ACTION_CHECK_DATA = "io.hashtek.clippers.action.CHECK_DATA";
    private static int clipboardData = 0;
    private SqliteListener listener = null;

    public ClipboardMonitor() {
        super("ClipboardMonitor");
        listener = new SqliteListener(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHECK_DATA.equals(action)) {

                final ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if ( clipboard.hasPrimaryClip() ) {
                    int total_count = clipboard.getPrimaryClip().getItemCount();

                    for ( int i = 0; i < total_count; i++ ) {
                        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(i);
                        String strItem = item.coerceToText(this).toString();

                        if (strItem.hashCode() != clipboardData) {
                            Log.d(TAG, strItem);
                            clipboardData = strItem.hashCode();
                            if ( clipboardData != 0 ) {
                                listener.update(strItem);
                                showPopup("Message copied");
                            }
                        }
                    }
                }
                scheduleNextUpdate(this);
            }
        }
    }

    public void showPopup(final String toastMessage) {
        if (SettingsManager.getInstance(this).getNotifyToast()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void scheduleNextUpdate(Context context) {
        if ( SettingsManager.getInstance(context).getServiceEnabled() ) {
            Intent alarmIntent = new Intent(context, ClipboardMonitor.class);
            alarmIntent.setAction(ACTION_CHECK_DATA);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarm.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 2500, pendingIntent);
        }
    }

}
