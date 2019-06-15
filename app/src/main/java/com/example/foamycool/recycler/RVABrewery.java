package com.example.foamycool.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foamycool.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;
import java.util.TreeMap;

import io.reactivex.subjects.PublishSubject;

public class RVABrewery extends ExpandableRecyclerViewAdapter<RVABrewery.BreweryRegionViewHolder, RVABrewery.BreweryViewHolder> {
    public PublishSubject<String> subject;

    public RVABrewery(List<? extends ExpandableGroup> groups) {
        super(groups);
        subject=PublishSubject.create();
    }

    @Override
    public BreweryRegionViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brewerry_region, parent, false);
        return new BreweryRegionViewHolder(view);
    }

    @Override
    public BreweryViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brewerry_item, parent, false);
        return new BreweryViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(BreweryViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Brewery brewery = ((BreweryByRegions) group).getItems().get(childIndex);
        holder.bind(brewery);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.onNext(brewery.id);
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(BreweryRegionViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.bind((BreweryByRegions) group);
    }


    class BreweryRegionViewHolder extends GroupViewHolder {
        private TextView region;

         BreweryRegionViewHolder(View itemView) {
            super(itemView);
            region=itemView.findViewById(R.id.brewery_region);
        }

         void bind(BreweryByRegions b){
            region.setText(b.getTitle());
        }
    }

    class BreweryViewHolder extends ChildViewHolder {
        private TextView name;

         BreweryViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.brewery_item_name);
        }

         void bind(Brewery b){
            name.setText(b.name);
        }
    }
}
