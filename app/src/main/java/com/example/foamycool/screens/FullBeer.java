package com.example.foamycool.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foamycool.R;
import com.example.foamycool.database.BeerRepository;
import com.example.foamycool.database.tables.Beer;
import com.example.foamycool.retrofit.RetrofitClient;
import com.example.foamycool.retrofit.RetrofitSingleBeerService;
import com.example.foamycool.retrofit.models.Beer.BeerData;
import com.example.foamycool.retrofit.models.Beer.SingleBase;
import com.example.foamycool.retrofit.models.Beer.Style;
import com.example.foamycool.screens.fragments.menu.SearchByKeywords;
import com.example.foamycool.utilities.BeerAction;
import com.example.foamycool.utilities.Network;
import com.example.foamycool.utilities.RxBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class FullBeer extends AppCompatActivity {
    final String url_link = "https://sandbox-api.brewerydb.com/v2/beer/*****" +
            "?key=d682baff76ee13d9ee6c94a92f92a1ea";
    private TextView category, description;
    private ImageButton likeButton;
    private Beer beer;
    private String beerId;
    private Disposable downloadSubscriber, likeOnClickSubscriber, getDataSubscriber;
    BeerRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_beer);
        beerId = getIntent().getStringExtra(SearchByKeywords.BEER_ID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        category = findViewById(R.id.beer_category);
        description = findViewById(R.id.beer_full_description);
        setLikeOnClick();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getData(beerId);
        initializeRepository();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (RxBus.getSubject().hasObservers()) {
            if (likeButton.getTag().equals("disliked")) {
                BeerAction ba = new BeerAction(beer.getId(), true);
                RxBus.getSubject().onNext(ba);
            }
        }
        downloadSubscriber.dispose();
        likeOnClickSubscriber.dispose();
        getDataSubscriber.dispose();
        super.onDestroy();
    }

    private void setLikeOnClick() {
        likeButton = findViewById(R.id.liked);
        final PublishSubject<View> subject = PublishSubject.create();
        likeOnClickSubscriber = subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<View>() {
            @Override
            public void accept(View v) {
                ImageButton button = (ImageButton) v;
                String tag = button.getTag().toString();
                setImageButtonState(tag);
                if (tag.equals("liked")) {
                    repository.delete(beer);
                } else {
                    repository.insert(beer);
                }
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.onNext(v);
            }
        });

    }

    private void setImageButtonState(String state) {
        if (state.equals("disliked")) {
            likeButton.setImageResource(R.drawable.ic_liked);
            likeButton.setTag("liked");
        } else {
            likeButton.setImageResource(R.drawable.ic_disliked);
            likeButton.setTag("disliked");
        }
    }

    private void initializeRepository() {
        repository = new BeerRepository(getApplication());
        getDataSubscriber = repository.subject.subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Beer>>() {
            @Override
            public void accept(List<Beer> beers) {
                if (beers.size() == 0) {
                    setImageButtonState("liked");
                } else {
                    setImageButtonState("disliked");
                }
            }
        });
        repository.checkBeer(beerId);
    }

    private void getData(String beerId) {
        PublishSubject<SingleBase> subject = PublishSubject.create();
        downloadSubscriber = subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<SingleBase>() {
            @Override
            public void accept(SingleBase singleBase) {
                BeerData data = singleBase.getData();
                if (data != null) {
                    setData(data);
                }
            }
        });
        Network.downloadData(RetrofitClient.getRetrofitInstance().create(RetrofitSingleBeerService.class)
                .getAll(url_link.replace("*****", beerId)), subject);
    }

    private void setData(BeerData data) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        beer = new Beer(data.getId(), data.getName(), data.getStyle() == null ? null : data.getStyle().getName(), data.getLabels() == null ? null : data.getLabels().getIcon());
        ((TextView) findViewById(R.id.toolbar_title)).setText(data.getName());
        Glide.with(this).load(data.getLabels() == null ? getDrawable(R.drawable.ic_image_not_found) : data.getLabels().getMedium()).into((ImageView) findViewById(R.id.beer_large_photo));
        SpannableString categoryName = new SpannableString(getString(R.string.category) + "\r\n\r\n");
        Style style = data.getStyle();
        if (style != null) {
            description.setText(style.getDescription());
            if (data.getStyle().getCategory() != null) {
                categoryName = new SpannableString(getString(R.string.category) + "\r\n\r\n" + data.getStyle().getCategory().getName());
                categoryName.setSpan(new RelativeSizeSpan(2f), 0, getString(R.string.category).length(), 0);
            }
        }
        category.setText(categoryName);

    }
}
