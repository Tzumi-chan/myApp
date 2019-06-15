package com.example.foamycool.retrofit;

import com.example.foamycool.retrofit.models.Beer.Base;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitBeerService {
    @GET
    Call<Base> getAll(@Url String url);
}
