package com.hajma.apps.mina2.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DuaListApiModel implements Parcelable {

    private int id;
    private int day;
    private String title;
    private String content;

    public DuaListApiModel() {}

    public DuaListApiModel(int id, int day, String title, String content) {
        this.id = id;
        this.day = day;
        this.title = title;
        this.content = content;
    }

    protected DuaListApiModel(Parcel in) {
        id = in.readInt();
        day = in.readInt();
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<DuaListApiModel> CREATOR = new Creator<DuaListApiModel>() {
        @Override
        public DuaListApiModel createFromParcel(Parcel in) {
            return new DuaListApiModel(in);
        }

        @Override
        public DuaListApiModel[] newArray(int size) {
            return new DuaListApiModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(day);
        dest.writeString(title);
        dest.writeString(content);
    }
}
