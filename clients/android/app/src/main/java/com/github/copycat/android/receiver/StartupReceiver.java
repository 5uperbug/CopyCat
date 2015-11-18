package com.github.copycat.android.receiver;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;

import com.github.copycat.android.service.ClipboardMonitor;

public class StartupReceiver extends BroadcastReceiver {
    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent bootIntent = new Intent(context, ClipboardMonitor.class);
        context.startService(bootIntent);
    }
}
