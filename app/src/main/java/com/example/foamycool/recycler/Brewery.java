package com.example.foamycool.recycler;

import android.os.Parcel;
import android.os.Parcelable;

public class Brewery implements Parcelable {
    public final String id;
    public final String name;

    public Brewery(String id, String name){
        this.id=id;
        this.name=name;
    }

    protected Brewery(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Brewery> CREATOR = new Creator<Brewery>() {
        @Override
        public Brewery createFromParcel(Parcel in) {
            return new Brewery(in);
        }

        @Override
        public Brewery[] newArray(int size) {
            return new Brewery[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
