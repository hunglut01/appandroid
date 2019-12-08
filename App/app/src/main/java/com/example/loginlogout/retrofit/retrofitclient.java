package com.example.loginlogout.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class retrofitclient {
    private  static Retrofit instance;

    public static Retrofit getInstance() {
        if(instance == null)
        {
            instance = new Retrofit.Builder()
                    .baseUrl("http://23.101.29.94:1111/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }
}
