package com.github.copycat.android.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.copycat.android.R;
import com.github.copycat.android.db.ClipboardModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoryListAdapter extends ArrayAdapter<ClipboardModel> {

    public HistoryListAdapter(Context context, ArrayList<ClipboardModel> source) {
        super(context, R.layout.historylist_view, source.toArray(new ClipboardModel[0]));
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        View v = view;
        ClipboardModel item = getItem(index);

        if ( v == null ) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.historylist_view, null);
        }

        TextView txtClipboard = (TextView) v.findViewById(R.id.txt_clipboard_data);
        TextView txtMetadata = (TextView) v.findViewById(R.id.txt_meta_text);

        Date date = new Date(item.getCreationDate());
        DateFormat format = new SimpleDateFormat("dd/MM HH:mm");
        format.setTimeZone(Calendar.getInstance().getTimeZone());

        txtClipboard.setText(item.getContents());
        txtMetadata.setText(format.format(date));

        return v;
    }
}
