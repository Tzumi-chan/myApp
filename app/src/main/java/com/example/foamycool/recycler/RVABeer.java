package com.example.foamycool.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foamycool.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class RVABeer extends RecyclerView.Adapter {
    public PublishSubject<String> subject;
    protected List<ModelForList> list = new ArrayList<>();
    private final int ITEM_VIEW_TYPE_CARD = 0, ITEM_VIEW_TYPE_BAR = 1;
    protected Context con;

    public RVABeer(Context con) {
        subject = PublishSubject.create();
        this.con = con;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh = null;
        switch (i) {
            case ITEM_VIEW_TYPE_CARD: {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_beer_item, viewGroup, false);
                vh = new CardViewHolder(v);
                break;
            }
            case ITEM_VIEW_TYPE_BAR: {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar, viewGroup, false);
                vh = new BarViewHolder(v);
                break;
            }
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CardViewHolder) {
            CardViewHolder vh = (CardViewHolder) viewHolder;
            Beer b = (Beer) list.get(i);
            String photo = b.photo;
            if (photo == null) {
                Glide.with(con).load(con.getResources().getDrawable(R.drawable.ic_image_not_found, null)).into(vh.photo);
            } else {
                Glide.with(con).load(photo).into(vh.photo);
            }
            SpannableString ss1 = new SpannableString(b.name + "\r\n" + b.shortDescr);
            ss1.setSpan(new RelativeSizeSpan(2f), 0, b.name.length(), 0);
            vh.description.setText(ss1);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? ITEM_VIEW_TYPE_BAR : ITEM_VIEW_TYPE_CARD;
    }

    public void deleteBar() {
        if (list.size() > 0)
            if (list.get(list.size() - 1) == null) {
                list.remove(list.size() - 1);
                notifyItemRemoved(list.size());
            }
    }

    public void deleteBeer(String id) {
        for (int i = 0; i < list.size(); i++) {
            if (((Beer) list.get(i)).id.equals(id)) {
                list.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void clearList() {
        int pos = list.size();
        list = new ArrayList<>();
        notifyItemRangeRemoved(0, pos);
    }

    public void setList(List<ModelForList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ModelForList> list) {
        int s = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(s, list.size());
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView photo;
        ImageView arrow;
        TextView description;

        CardViewHolder(View v) {
            super(v);
            card = v.findViewById(R.id.card_beer_item);
            photo = v.findViewById(R.id.beer_photo);
            arrow = v.findViewById(R.id.arrow);
            Glide.with(con).load(con.getDrawable(R.drawable.ic_simple_arrow)).into(arrow);
            description = v.findViewById(R.id.beer_description);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != -1)
                        subject.onNext(((Beer) list.get(pos)).id);
                }
            });
        }
    }

    class BarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar bar;

        BarViewHolder(@NonNull View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.bar_for_recycle);
        }
    }
}
