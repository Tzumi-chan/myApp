package com.example.foamycool.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foamycool.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import uk.co.deanwild.flowtextview.FlowTextView;

public class RVABreweryFullInfo extends RVABeer {
    private final int ITEM_VIEW_TYPE_BEER = 0, ITEM_VIEW_TYPE_BREWERY= 1;

    public RVABreweryFullInfo(List<ModelForList> list,Context con) {
        super(con);
        subject = PublishSubject.create();
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh = null;
        switch (i) {
            case ITEM_VIEW_TYPE_BEER: {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_beer_item, viewGroup, false);
                vh = new CardViewHolder(v);
                break;
            }
            case ITEM_VIEW_TYPE_BREWERY: {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brewery_icon_and_description, viewGroup, false);
                vh = new BreweryViewHolder(v);
                break;
            }
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CardViewHolder) {
            CardViewHolder vh = (CardViewHolder) viewHolder;
            Beer b = (Beer)list.get(i);
            String photo = b.photo;
            if (photo == null) {
                Glide.with(con).load(con.getResources().getDrawable(R.drawable.ic_image_not_found, null)).into(vh.photo);
            } else {
                Glide.with(con).load(photo).into(vh.photo);
            }
            SpannableString ss1 = new SpannableString(b.name + "\r\n" + b.shortDescr);
            ss1.setSpan(new RelativeSizeSpan(2f), 0, b.name.length(), 0);
            vh.description.setText(ss1);
        }else{
            BreweryViewHolder vh=(BreweryViewHolder)viewHolder;
            BreweryDescription brewery=(BreweryDescription)list.get(i);
            String photo=brewery.photo;
            if (photo == null) {
                Glide.with(con).load(con.getResources().getDrawable(R.drawable.ic_image_not_found, null)).into(vh.icon);
            } else {
                Glide.with(con).load(photo).into(vh.icon);
            }
            vh.description.setText(brewery.shortDescr);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) instanceof Beer ? ITEM_VIEW_TYPE_BEER : ITEM_VIEW_TYPE_BREWERY;
    }

    public void addListOnPos(int pos,ModelForList brewery){
        list.add(pos,brewery);
        notifyItemInserted(pos);
    }

    class BreweryViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        FlowTextView description;

         BreweryViewHolder(@NonNull View v) {
            super(v);
            icon = v.findViewById(R.id.brewery_icon);
            description = v.findViewById(R.id.ftv);
        }
    }

}
