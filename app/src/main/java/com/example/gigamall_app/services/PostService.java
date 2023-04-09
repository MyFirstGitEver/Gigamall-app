package com.example.gigamall_app.services;

import com.example.gigamall_app.entities.PostEntity;
import com.example.gigamall_app.tools.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PostService {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.MINUTES)
            .build();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    PostService service = new Retrofit.Builder()
            .baseUrl(Tools.DOMAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(PostService.class);

    @GET("api/posts/{brandId}")
    Call<List<PostEntity>> fetchPosts(@Path("brandId") int id);

    @GET("api/posts/random")
    Call<List<PostEntity>> fetchRandomPosts(@Header("ids") String ids);

    @GET("api/posts/top6")
    Call<List<PostEntity>> fetchTop6();
}