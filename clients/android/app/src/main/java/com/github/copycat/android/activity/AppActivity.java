package com.github.copycat.android.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.copycat.android.R;
import com.github.copycat.android.core.HistoryListAdapter;
import com.github.copycat.android.core.SettingsManager;
import com.github.copycat.android.db.ClipboardDatastore;
import com.github.copycat.android.db.ClipboardModel;
import com.github.copycat.android.service.ClipboardMonitor;

import java.util.ArrayList;
import java.util.Collections;

public class AppActivity extends Activity {

    private static final String TAG = AppActivity.class.getCanonicalName();
    private ListView historyList;

    private void populateHistoryView() {
        ClipboardDatastore db = new ClipboardDatastore(this);
        ArrayList<ClipboardModel> dbEntries = db.getAllEntries();
        Collections.reverse(dbEntries);

        historyList.setAdapter(new HistoryListAdapter(this, dbEntries));

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Item clicked");
                ClipboardModel selectedItem = (ClipboardModel) adapterView.getItemAtPosition(i);
                String itemText = selectedItem.getContents();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                ClipDescription clipDescription = new ClipDescription("TEXT", new String[] {
                        ClipDescription.MIMETYPE_TEXT_PLAIN
                });
                ClipData clipData = new ClipData(clipDescription, new ClipData.Item(itemText));
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Message copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        historyList = (ListView) findViewById(R.id.historyList);
        populateHistoryView();

        ClipboardMonitor.scheduleNextUpdate(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateHistoryView();
        ClipboardMonitor.scheduleNextUpdate(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.isCheckable() ) {
            item.setChecked(!item.isChecked());
            switch (item.getItemId()) {
                case R.id.action_toggle_toast:
                    SettingsManager.getInstance(this).setNotifyToast(item.isChecked());
                    break;

                case R.id.action_toggle_service:
                    SettingsManager.getInstance(this).setServiceEnabled(item.isChecked());
                    break;

                default:
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
