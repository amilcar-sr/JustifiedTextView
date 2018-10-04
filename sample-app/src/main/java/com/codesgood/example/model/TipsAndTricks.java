package com.codesgood.example.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TipsAndTricks implements Parcelable {

    private final static String TAG = "TipsAndTricks";

   private String name;
   private String lastName;
   private int age;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.lastName);
        dest.writeInt(this.age);
    }

    public TipsAndTricks() {
    }

    protected TipsAndTricks(Parcel in) {
        this.name = in.readString();
        this.lastName = in.readString();
        this.age = in.readInt();
    }

    public static final Parcelable.Creator<TipsAndTricks> CREATOR = new Parcelable.Creator<TipsAndTricks>() {
        @Override
        public TipsAndTricks createFromParcel(Parcel source) {
            return new TipsAndTricks(source);
        }

        @Override
        public TipsAndTricks[] newArray(int size) {
            return new TipsAndTricks[size];
        }
    };
}
