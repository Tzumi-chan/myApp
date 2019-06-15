package com.example.foamycool.screens.fragments.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foamycool.R;
import com.example.foamycool.recycler.Beer;
import com.example.foamycool.recycler.ModelForList;
import com.example.foamycool.recycler.RVABeer;
import com.example.foamycool.retrofit.RetrofitBeerService;
import com.example.foamycool.retrofit.RetrofitClient;
import com.example.foamycool.retrofit.models.Beer.Base;
import com.example.foamycool.retrofit.models.Beer.BeerData;
import com.example.foamycool.screens.FullBeer;
import com.example.foamycool.utilities.Network;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class SearchByKeywords extends Fragment {
    private RVABeer adapter;
    private TextView noBeer;
    private Disposable searchSubscriber, loadMoreSubscriber, itemClickSubscriber;
    private int page = 1;
    private String search;
    final String urlLink = "https://sandbox-api.brewerydb.com/v2/beers" +
            "?key=d682baff76ee13d9ee6c94a92f92a1ea" +
            "&name=*****" + "&p=";
    public final static String BEER_ID = "com.example.foamycool.screens.fragments.menu.BEER_ID";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_by_keywords, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        noBeer = view.findViewById(R.id.no_beer);
        initializeRecycler(view);
        initializeSearchBar(view);
    }

    @Override
    public void onDestroyView() {
        loadMoreSubscriber.dispose();
        searchSubscriber.dispose();
        itemClickSubscriber.dispose();
        super.onDestroyView();
    }

    private void initializeSearchBar(View view) {
        SearchView searchBar = view.findViewById(R.id.search_bar);
        final PublishSubject<Base> subject = PublishSubject.create();
        searchSubscriber = subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Base>() {
            @Override
            public void accept(Base response) {
                List<ModelForList> list = dataToModel(response);
                if (list.size() == 0) {
                    noBeer.setVisibility(View.VISIBLE);
                } else {
                    noBeer.setVisibility(View.INVISIBLE);
                }
                adapter.setList(list);
            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() < 4) {
                    Toast.makeText(getContext(),
                            getContext().getResources().getString(R.string.wrong_search), Toast.LENGTH_LONG).show();
                    return false;
                }
                page = 1;
                search = s;
                adapter.clearList();
                getData(subject, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void getData(final PublishSubject<Base> subject, String s) {
        addProgressBar(subject);
        Network.downloadData(RetrofitClient.getRetrofitInstance().create(RetrofitBeerService.class)
                .getAll(urlLink.replace("*****", "*" + s + "*") + page), subject);
        page++;
    }

    private void addProgressBar(PublishSubject<Base> subject) {
        List<BeerData> l = new ArrayList<>(1);
        l.add(null);
        Base b = new Base();
        b.setData(l);
        subject.onNext(b);
    }

    private void initializeRecycler(View view) {
        RecyclerView rv = view.findViewById(R.id.action_search_by_keywords_list);
        Context con = getContext();
        adapter = new RVABeer(con);

        itemClickSubscriber = adapter.subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Intent intent = new Intent(getActivity(), FullBeer.class);
                intent.putExtra(BEER_ID, s);
                startActivity(intent);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(con));
        rv.setAdapter(adapter);
        final PublishSubject<Base> subject = PublishSubject.create();

        loadMoreSubscriber = subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Base>() {
            @Override
            public void accept(Base response) {
                adapter.deleteBar();
                adapter.addList(dataToModel(response));

            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter.getItemCount() > 1) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                        getData(subject, search);
                    }
                }
            }
        });
    }

    private List<ModelForList> dataToModel(Base response) {
        List<ModelForList> list = new ArrayList<>();
        if (response != null) {
            List<BeerData> data = response.getData();
            if (data != null) {
                list = new ArrayList<>(data.size());
                for (BeerData beer : data) {
                    if (beer == null) {
                        list.add(null);
                    } else {
                        list.add(new Beer(beer.getId(), beer.getLabels() == null ? null : beer.getLabels().getIcon(), beer.getName(), beer.getStyle() == null ? null : beer.getStyle().getName()));
                    }
                }
            }
        }
        return list;
    }
}
