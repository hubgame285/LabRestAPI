package com.mgok.my_lab;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DistributorApi {

    DistributorApi api = RetrofitClient.getInstance().create(DistributorApi.class);

    @GET("distributor/get-list-distributor")
    Call<MyResponse<ArrayList<Distributor>>> getListDistributor();

    @GET("distributor/search-distributor")
    Call<MyResponse<ArrayList<Distributor>>> searchDistributor(@Query("query") String query);

    @POST("distributor/add-distributor")
    Call<MyResponse<Distributor>> addDistributor(@Body() RequestBody name);

    @DELETE("distributor/delete-distributor-by-id/{id}")
    Call<MyResponse<Distributor>> deleteDistributorById(@Path("id") String id);

}
