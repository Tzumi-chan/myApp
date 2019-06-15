package com.example.foamycool.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.foamycool.database.tables.Beer;

import java.util.List;

@Dao
public interface BeerDao {
    @Insert
    void insert(Beer beer);

    @Delete
    void deleteBeer(Beer beer);

    @Query("SELECT * from beer_table ORDER BY beer_name ASC")
    List<Beer> getAllBeers();

    @Query("SELECT * from beer_table WHERE beer_id=:beerId ORDER BY beer_name ASC")
    List<Beer> checkBeer(String beerId);
}
