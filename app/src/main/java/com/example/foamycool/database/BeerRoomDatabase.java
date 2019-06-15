package com.example.foamycool.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.foamycool.database.tables.Beer;

@Database(entities = {Beer.class}, version = 1, exportSchema = false)
public abstract class BeerRoomDatabase extends RoomDatabase {
    public abstract BeerDao beerDao();
    private static BeerRoomDatabase INSTANCE;

    static BeerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BeerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BeerRoomDatabase.class, "beer_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
