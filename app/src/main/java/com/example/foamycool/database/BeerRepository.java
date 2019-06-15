package com.example.foamycool.database;

import android.app.Application;
import android.util.Log;

import com.example.foamycool.database.tables.Beer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.subjects.PublishSubject;

public class BeerRepository {
    private BeerDao mBeerDao;
    private ExecutorService ex_s;
    public PublishSubject<List<Beer>> subject;

    public BeerRepository(Application application) {
        BeerRoomDatabase db = BeerRoomDatabase.getDatabase(application);
        mBeerDao = db.beerDao();
        ex_s = Executors.newCachedThreadPool();
        subject = PublishSubject.create();
    }

    public void getAllBeers() {
        ex_s.execute(new Runnable() {
            @Override
            public void run() {
                subject.onNext(mBeerDao.getAllBeers());
            }
        });
    }

    public void insert(final Beer beer) {
        ex_s.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mBeerDao.insert(beer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void delete(final Beer beer) {
        ex_s.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mBeerDao.deleteBeer(beer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkBeer(final String id) {
        ex_s.execute(new Runnable() {
            @Override
            public void run() {
                subject.onNext(mBeerDao.checkBeer(id));
            }
        });
    }
}
