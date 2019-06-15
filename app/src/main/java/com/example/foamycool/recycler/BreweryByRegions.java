package com.example.foamycool.recycler;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class BreweryByRegions extends ExpandableGroup<Brewery> {
    public BreweryByRegions(String title, List<Brewery> items){
        super(title, items);
    }
}
