//package com.elblasy.navigation.api;
//
//import com.elblasy.navigation.models.PlacesResults;
//
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.Query;
//
//public interface GoogleMapAPI {
//
//    @GET("place/findplacefromtext/json")
//    Call<PlacesResults> getNearBy(
//            @Query("location") String location,
//            @Query("radius") int radius,
//            @Query("type") String type,
//            @Query("keyword") String keyword,
//            @Query("key") String key
//    );
//}
