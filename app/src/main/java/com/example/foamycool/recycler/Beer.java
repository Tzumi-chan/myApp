package com.example.foamycool.recycler;

public class Beer implements ModelForList{
    public final String id;
    public final String photo;
    public final String name;
    public final String shortDescr;

    public Beer(String id,String photo, String name, String description){
        this.id=id;
        this.photo=photo;
        this.name=name;
        this.shortDescr=description;
    }
}
