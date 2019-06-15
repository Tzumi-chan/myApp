package com.example.foamycool.retrofit;

import com.example.foamycool.retrofit.models.Brewery.Base;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitBreweriesService {
    @GET
    Call<Base> getAll(@Url String url);
}
