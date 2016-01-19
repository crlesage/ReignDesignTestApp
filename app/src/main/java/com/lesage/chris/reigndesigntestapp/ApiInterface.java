package com.lesage.chris.reigndesigntestapp;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Chris on 1/15/2016.
 */
public interface ApiInterface {
    /* Get Data */
    @GET("/api/v1/search_by_date")
    Call<JsonData> getData (
            @Query("query") String query
    );
}
