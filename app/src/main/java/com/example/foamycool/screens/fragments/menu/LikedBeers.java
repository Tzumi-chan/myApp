package com.example.foamycool.screens.fragments.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foamycool.R;
import com.example.foamycool.database.BeerRepository;
import com.example.foamycool.database.tables.Beer;
import com.example.foamycool.recycler.ModelForList;
import com.example.foamycool.recycler.RVABeer;
import com.example.foamycool.screens.FullBeer;
import com.example.foamycool.utilities.BeerAction;
import com.example.foamycool.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LikedBeers extends Fragment {
    private RVABeer adapter;
    private TextView noBeer;
    private Disposable itemClickSubscriber, searchSubscriber, listItemsStateSubscriber;
    BeerRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liked_beers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        noBeer = view.findViewById(R.id.no_liked_beers);
        getData();
        initializeRecycler(view);
    }

    @Override
    public void onDestroyView() {
        searchSubscriber.dispose();
        itemClickSubscriber.dispose();
        listItemsStateSubscriber.dispose();
        super.onDestroyView();
    }

    private void getData() {
        repository = new BeerRepository(getActivity().getApplication());
        searchSubscriber = repository.subject.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Beer>>() {
                    @Override
                    public void accept(List<Beer> beers) {
                        List<ModelForList> list = new ArrayList<>(beers.size());
                        for (Beer beer : beers) {
                            list.add(new com.example.foamycool.recycler.Beer(beer.getId(), beer.getIcon(), beer.getName(), beer.getCategory()));
                        }
                        if(list.size()==0){
                            noBeer.setVisibility(View.VISIBLE);
                        }else{
                        adapter.setList(list);
                            noBeer.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        repository.getAllBeers();
    }


    private void initializeRecycler(View view) {
        RecyclerView rv = view.findViewById(R.id.liked_beer_list);
        Context con = getContext();
        adapter = new RVABeer(con);

        itemClickSubscriber = adapter.subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Intent intent = new Intent(getActivity(), FullBeer.class);
                intent.putExtra(SearchByKeywords.BEER_ID, s);
                startActivity(intent);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(con));
        rv.setAdapter(adapter);
        listItemsStateSubscriber = RxBus.getSubject().
                subscribeWith(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        if (o instanceof BeerAction) {
                            BeerAction b = (BeerAction) o;
                            if (b.delete) {
                                adapter.deleteBeer(b.beer);
                                if(adapter.getItemCount()==0){
                                    noBeer.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
