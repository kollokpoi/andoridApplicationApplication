package com.example.bottom.services;

import static com.example.bottom.MainActivity.BASE_URL;
import static com.example.bottom.MainActivity.userToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAuthorizeService {


    public static Retrofit getRetrofit(){
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer "+userToken)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
