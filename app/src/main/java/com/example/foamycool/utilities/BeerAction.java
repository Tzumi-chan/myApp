package com.example.foamycool.utilities;

import com.example.foamycool.database.tables.Beer;

public class BeerAction {
    public String beer;
    public boolean delete;

    public BeerAction(String beer, boolean delete){
        this.beer=beer;
        this.delete=delete;
    }
}
