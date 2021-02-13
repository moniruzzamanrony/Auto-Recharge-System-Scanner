package com.itvillage.ars.api;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Apis {
    @POST("api/users/add")
    Observable<String> addNewUser(@Body RequestBody body);

}
