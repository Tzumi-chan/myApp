package com.example.foamycool.screens.fragments.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foamycool.R;
import com.example.foamycool.recycler.Brewery;
import com.example.foamycool.recycler.BreweryByRegions;
import com.example.foamycool.recycler.RVABrewery;
import com.example.foamycool.retrofit.RetrofitBreweriesService;
import com.example.foamycool.retrofit.RetrofitClient;
import com.example.foamycool.retrofit.models.Brewery.Base;
import com.example.foamycool.retrofit.models.Brewery.Datum;
import com.example.foamycool.retrofit.models.Brewery.Location;
import com.example.foamycool.screens.FullBrewery;
import com.example.foamycool.utilities.Network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchByLocation extends Fragment {
    private TextView noBrewery;
    private RecyclerView rv;
    private Disposable searchSubscriber, onBreweryClickSubscriber;
    public final static String BREWERY_ID = "com.example.foamycool.screens.fragments.menu.BREWERY_ID";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_by_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        noBrewery = view.findViewById(R.id.no_brewery);
        initializeRecycler(view);
    }

    @Override
    public void onDestroyView() {
        onBreweryClickSubscriber.dispose();
        searchSubscriber.dispose();
        super.onDestroyView();
    }

    private void initializeRecycler(View view) {
        rv = view.findViewById(R.id.search_by_location_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
    }

    private void getData() {
        final PublishSubject<Base> subject = PublishSubject.create();
        searchSubscriber = subject.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Base>() {
                    @Override
                    public void accept(Base response) {
                        List<Datum> data = response.getData();
                        if (data != null) {
                            List<BreweryByRegions> list = new LinkedList<>();
                            TreeMap<String, List<Brewery>> result = new TreeMap<>();
                            for (Datum brewery : data) {
                                String company = brewery.getName();
                                String companyId = brewery.getId();
                                List<Location> locations = brewery.getLocations();
                                if (locations != null) {
                                    for (Location location : locations) {
                                        List<Brewery> list1 = new ArrayList<>();
                                        String region = location.getRegion();
                                        if (region != null) {
                                            if (result.containsKey(region))
                                                list1 = result.get(region);
                                            list1.add(new Brewery(companyId, company + " : " + location.getName()));
                                            result.put(region, list1);
                                        }
                                    }
                                }
                            }
                            for (TreeMap.Entry<String, List<Brewery>> e : result.entrySet()) {
                                list.add(new BreweryByRegions(e.getKey(), e.getValue()));
                            }

                            if (list.size() == 0) {
                                noBrewery.setVisibility(View.VISIBLE);
                            } else {
                                noBrewery.setVisibility(View.INVISIBLE);
                            }
                            RVABrewery adapter = new RVABrewery(list);
                            onBreweryClickSubscriber = adapter.subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) {
                                    Intent intent = new Intent(getActivity(), FullBrewery.class);
                                    intent.putExtra(BREWERY_ID, s);
                                    startActivity(intent);
                                }
                            });
                            rv.setAdapter(adapter);
                        }

                    }
                });

        Network.downloadData(RetrofitClient.getRetrofitInstance().create(RetrofitBreweriesService.class)
                        .getAll("https://sandbox-api.brewerydb.com/v2/breweries?key=d682baff76ee13d9ee6c94a92f92a1ea&withLocations=y")
                , subject);
    }
}
