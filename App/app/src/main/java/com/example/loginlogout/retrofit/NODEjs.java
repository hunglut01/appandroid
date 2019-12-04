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
                                    @Field("email") String email,
                                    @Field("password") String password);
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("username") String username,
                                    @Field("password") String password);
    @POST("ReqresetPass")
    @FormUrlEncoded
    Observable<String> reqresetpass(@Field("email") String username);

    @POST("resetPass")
    @FormUrlEncoded
    Observable<String> resetpass(@Field("code") String code,
                                 @Field("password") String password,
                                 @Field("verifypassword") String verifypassword);
    @GET("searchav/{word}")
    Observable<String> Searchav(@Path("word") String word);

    @GET("searchva/{word}")
    Observable<String> Searchva(@Path("word") String word);

    @GET("saveword/{id}/{word}/{html}")
    Observable<String> saveWord(@Path("id") String id,
                                @Path("word") String word,
                                @Path("html") String html);
    @GET("loadword/{id}")
    Observable<String> loadWord(@Path("id") String id);

    @GET("deleteword/{id}")
    Observable<String> deleteWord(@Path("id") String id);

    @GET("savewordva/{id}/{word}/{html}")
    Observable<String> saveWordva(@Path("id") String id,
                                @Path("word") String word,
                                @Path("html") String html);
    @GET("loadwordva/{id}")
    Observable<String> loadWordva(@Path("id") String id);

    @GET("deletewordva/{id}")
    Observable<String> deleteWordva(@Path("id") String id);

    @GET("irregular")
    Observable<String> Irregular();

    @GET("getallreading")
    Observable<String> getallreading();

    @GET("grammar")
    Observable<String> grammar();

    @GET("listening_conversation")
    Observable<String> listening();

    @GET("listening_photo")
    Observable<String> listeningbyphoto();

    @GET("savescore/{id}/{type}/{score}")
    Observable<String> saveScore(@Path("id") String id,
                                 @Path("type") String type,
                                 @Path("score") String score);
}
