package com.example.foamycool.recycler;

public class BreweryDescription implements ModelForList{
    public final String photo;
    public final String shortDescr;

    public BreweryDescription(String photo, String description){
        this.photo=photo;
        this.shortDescr=description;
    }
}
