package com.mydev.swipetoeat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpService {
    @GET("businesses/search")
    fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("open_at") open_at: Int,
        @Query("categories") restaurantCategory: String,
        @Query("location") location:String) : Call<YelpSearchResult>
}