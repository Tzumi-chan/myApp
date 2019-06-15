package com.example.foamycool.retrofit;

import com.example.foamycool.retrofit.models.Brewery.Base;
import com.example.foamycool.retrofit.models.Brewery.SingleBase;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitSingleBreweryService {
    @GET
    Call<SingleBase> getAll(@Url String url);
}
