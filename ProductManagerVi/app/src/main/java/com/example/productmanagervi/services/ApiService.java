package com.example.productmanagervi.services;


import com.example.productmanagervi.model.Distributor;
import com.example.productmanagervi.model.Response;

import java.util.ArrayList;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    String BASE_URL = "http://192.168.1.16:3000/";
    @GET("distributors/list")
    Call<Response<ArrayList<Distributor>>> getListDistributor();

    @POST("distributors/add")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);
}
