package com.example.productmanagervi.services;


import com.example.productmanagervi.model.Distributor;
import com.example.productmanagervi.model.Response;
import com.example.productmanagervi.model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String BASE_URL = "http://172.16.52.29:3000/";
    @GET("distributors/list")
    Call<Response<ArrayList<Distributor>>> getListDistributor();

    @POST("distributors/add")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);
    @PUT("distributors/edit/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id") String id, @Body Distributor distributor);
    @DELETE("distributors/delete/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id") String id);
    @GET("distributors/search")
    Call<Response<ArrayList<Distributor>>> searchDistributor(@Query("key") String key);

    @Multipart
    @POST("users/add")
    Call<Response<User>> register(@Part("username")RequestBody username,
                                  @Part("password")RequestBody password,
                                  @Part("email")RequestBody email,
                                  @Part("name")RequestBody name,
                                  @Part("age")RequestBody age,
                                  @Part("available")RequestBody available,
                                  @Part MultipartBody.Part avatar
                                  );

    @POST("login/checkLogin")
    Call<Response<User>> checkLogin(@Body User user);
}
