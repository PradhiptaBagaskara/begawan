package com.pt.begawanpolosoro.adapter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InitRetro {
    public static String API_URL = "http://192.168.1.100:8080/";
//    public static String API_URL = "http://192.168.0.16/";

    private  static Retrofit retrofit = null;

    public static Retrofit InitApi() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }






}