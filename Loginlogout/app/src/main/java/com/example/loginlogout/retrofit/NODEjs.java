package com.example.loginlogout.retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NODEjs {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("username") String username,
                                    @Field("password") String password);
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("username") String username,
                                    @Field("password") String password);
    @GET("search/{word}")
    Observable<String> Search(@Path("word") String word);

    @GET("irregular/{a}")
    Observable<String> Irregular(@Path("a") String a);

    @GET("getallreading/{a}")
    Observable<String> getallreading(@Path("a") String a);
}