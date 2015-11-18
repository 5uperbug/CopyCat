package com.github.copycat.android.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

public class ClipboardModel implements Parcelable {

    private long id;
    private String clipboardContents;
    private long creationDate;

    public ClipboardModel(Parcel in) {
        id = in.readLong();
        clipboardContents = in.readString();
        creationDate = in.readLong();
    }

    public ClipboardModel(long row_id, String contents, long created) {
        setContents(contents);
        setCreationDate(created);
        this.id = row_id;
    }

    public ClipboardModel(String contents, long creationDate) {
        setContents(contents);
        setCreationDate(creationDate);
        this.id = -1;
    }

    public static final Creator<ClipboardModel> CREATOR = new Creator<ClipboardModel>() {
        @Override
        public ClipboardModel createFromParcel(Parcel in) {
            return new ClipboardModel(in);
        }

        @Override
        public ClipboardModel[] newArray(int size) {
            return new ClipboardModel[size];
        }
    };

    @Override
    public String toString() {
        return getContents();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(clipboardContents);
        parcel.writeLong(creationDate);
    }

    public void setContents(String contents) {
        this.clipboardContents = contents;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getContents() {
        return clipboardContents;
    }

    public long getId() {
        return id;
    }

    public long getCreationDate() {
        return creationDate;
    }

}
