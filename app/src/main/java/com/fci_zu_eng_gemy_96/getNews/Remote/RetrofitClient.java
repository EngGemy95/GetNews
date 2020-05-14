package com.fci_zu_eng_gemy_96.getNews.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null ;

    public static Retrofit getRetrofitClient(String baseUrl){
       if (retrofit == null){
           retrofit = new Retrofit.Builder()
                   .baseUrl(baseUrl)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
       }
       return retrofit ;
    }
}
