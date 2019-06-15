package com.example.foamycool.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.foamycool.R;
import com.example.foamycool.recycler.Beer;
import com.example.foamycool.recycler.BreweryDescription;
import com.example.foamycool.recycler.ModelForList;
import com.example.foamycool.recycler.RVABreweryFullInfo;
import com.example.foamycool.retrofit.RetrofitBeerService;
import com.example.foamycool.retrofit.RetrofitClient;
import com.example.foamycool.retrofit.RetrofitSingleBreweryService;
import com.example.foamycool.retrofit.models.Beer.Base;
import com.example.foamycool.retrofit.models.Beer.BeerData;
import com.example.foamycool.retrofit.models.Brewery.Datum;
import com.example.foamycool.retrofit.models.Brewery.SingleBase;
import com.example.foamycool.screens.fragments.menu.SearchByKeywords;
import com.example.foamycool.screens.fragments.menu.SearchByLocation;
import com.example.foamycool.utilities.Network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullBrewery extends AppCompatActivity {
    private Disposable searchSubscriber, itemClickSubscriber, beerSubscriber;
    private RVABreweryFullInfo adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_brewery_info);
        String breweryId = getIntent().getStringExtra(SearchByLocation.BREWERY_ID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeRecycler();
        getData(breweryId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onDestroy() {
        searchSubscriber.dispose();
        itemClickSubscriber.dispose();
        beerSubscriber.dispose();
        super.onDestroy();
    }

    private void initializeRecycler() {
        RecyclerView rv = findViewById(R.id.brewery_full_info);
        adapter = new RVABreweryFullInfo(new LinkedList<ModelForList>(), this);

        itemClickSubscriber = adapter.subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Intent intent = new Intent(getApplicationContext(), FullBeer.class);
                intent.putExtra(SearchByKeywords.BEER_ID, s);
                startActivity(intent);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void getData(String id) {
        final PublishSubject<SingleBase> subject = PublishSubject.create();
        searchSubscriber = subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<SingleBase>() {
            @Override
            public void accept(SingleBase response) {
                Datum data = response.getData();
                if (data != null) {
                    getSupportActionBar().setTitle(data.getName());
                    BreweryDescription brewery = new BreweryDescription(data.getImages() == null ? null : data.getImages().getMedium(), data.getDescription());
                    if (adapter.getItemCount() == 0) {
                        List<ModelForList> listBrewery = new LinkedList<>();
                        listBrewery.add(brewery);
                        adapter.setList(listBrewery);
                    } else {
                        adapter.addListOnPos(0, brewery);
                    }
                }
            }
        });
        Network.downloadData(RetrofitClient.getRetrofitInstance().create(RetrofitSingleBreweryService.class)
                        .getAll("https://sandbox-api.brewerydb.com/v2//brewery/" + id + "?key=d682baff76ee13d9ee6c94a92f92a1ea")
                , subject);
        final PublishSubject<Base> subject2 = PublishSubject.create();
        beerSubscriber = subject2.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Base>() {
            @Override
            public void accept(Base response) {
                List<BeerData> data = response.getData();
                if (data != null) {
                    List<ModelForList> list = new ArrayList<>(data.size());
                    for (BeerData beer : data) {
                        list.add(new Beer(beer.getId(), beer.getLabels() == null ? null : beer.getLabels().getIcon(), beer.getName(), beer.getStyle() == null ? null : beer.getStyle().getName()));
                    }

                    adapter.addList(list);
                }

            }
        });
        Network.downloadData(RetrofitClient.getRetrofitInstance().create(RetrofitBeerService.class)
                        .getAll("https://sandbox-api.brewerydb.com/v2//brewery/" + id + "/beers?key=d682baff76ee13d9ee6c94a92f92a1ea")
                , subject2);
    }
}
