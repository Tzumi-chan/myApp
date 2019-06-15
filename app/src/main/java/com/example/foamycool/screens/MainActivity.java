package com.example.foamycool.screens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.foamycool.R;
import com.example.foamycool.screens.fragments.menu.LikedBeers;
import com.example.foamycool.screens.fragments.menu.SearchByKeywords;
import com.example.foamycool.screens.fragments.menu.SearchByLocation;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    private int selectedFragment;
    private Disposable menuItemSubscriber;
    private static final String FRAGMENT_ID="com.example.foamycool.FRAGMENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedFragment=R.id.action_search_by_keywords;
        initializeMenu(selectedFragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_ID,selectedFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        menuItemSubscriber.dispose();
    }

    private void initializeMenu(int id) {
        BottomNavigationView menu = findViewById(R.id.bottom_navigation);
        menu.setItemIconTintList(null);
        MenuItem item=menu.getMenu().findItem(id);
        if(item!=null){
            item.setChecked(true);
        }
        final PublishSubject<Integer> subject = PublishSubject.create();
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                subject.onNext(menuItem.getItemId());
                return true;
            }
        });
        menuItemSubscriber=subject.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                selectFragment(integer);
            }
        });
        selectFragment(id);
    }

    private void selectFragment(int id){
        selectedFragment=id;
        Class fragmentClass;
        Fragment fragment = null;
        switch (id) {
            case R.id.action_favorites:
                fragmentClass = LikedBeers.class;
                break;
            case R.id.action_search_by_keywords:
                fragmentClass = SearchByKeywords.class;
                break;
            case R.id.action_search_by_location:
                fragmentClass = SearchByLocation.class;
                break;
            default:
                fragmentClass = SearchByKeywords.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setFragment(fragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
}
