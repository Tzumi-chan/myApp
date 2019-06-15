package com.example.foamycool.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new retrofit2.Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("https://sandbox-api.brewerydb.com/v2/beers/")
                    .build();
        }
        return retrofit;
    }
}
