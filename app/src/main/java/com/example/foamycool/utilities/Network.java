package com.example.foamycool.utilities;

import android.util.Log;

import com.example.foamycool.retrofit.RetrofitClient;
import com.example.foamycool.retrofit.RetrofitSingleBeerService;
import com.example.foamycool.retrofit.models.Beer.BeerData;
import com.example.foamycool.retrofit.models.Beer.SingleBase;

import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Network {

    public static<S> void downloadData(Call<S> call, final PublishSubject<S> subject){

        call.enqueue(new Callback<S>() {
            @Override
            public void onResponse(Call<S> call, Response<S> response) {
                if (response.body() != null) {
                    subject.onNext(response.body());
                }
            }

            @Override
            public void onFailure(Call<S> call, Throwable t) {
                Log.e("My", t.getMessage());
            }
        });
    }

}
