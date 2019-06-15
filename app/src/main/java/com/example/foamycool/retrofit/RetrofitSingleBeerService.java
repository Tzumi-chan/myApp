package com.example.foamycool.retrofit;

import com.example.foamycool.retrofit.models.Beer.Base;
import com.example.foamycool.retrofit.models.Beer.SingleBase;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitSingleBeerService {
    @GET
    Call<SingleBase> getAll(@Url String url);
}
