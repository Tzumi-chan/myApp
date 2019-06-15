package com.example.foamycool.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "beer_table")
public class Beer {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "beer_id")
    private String mId;

    @ColumnInfo(name = "beer_name")
    private String mName;

    @ColumnInfo(name = "beer_category")
    private String mCategory;

    @ColumnInfo(name = "beer_icon")
    private String mIcon;

    public Beer(@NonNull String id, String name, String mCategory,String icon) {
        this.mId = id;
        this.mName = name;
        this.mCategory = mCategory;
        this.mIcon = icon;
    }

    public String getId(){return this.mId;}
    public String getName(){return this.mName;}
    public String getCategory(){return this.mCategory;}
    public String getIcon(){return this.mIcon;}
}
