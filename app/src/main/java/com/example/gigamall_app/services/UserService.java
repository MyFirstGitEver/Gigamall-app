package com.example.gigamall_app.services;

import com.example.gigamall_app.tools.Tools;
import com.example.gigamall_app.entities.UserEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.MINUTES)
            .build();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    UserService service = new Retrofit.Builder()
            .baseUrl(Tools.DOMAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(UserService.class);

    @POST("api/user/register/{usingGoogle}")
    Call<Integer> register(
            @Body UserEntity user,
            @Path("usingGoogle") int usingGoogle);

    @DELETE("api/user/verify/{id}/{code}")
    Call<Integer> verify(@Path("id") int id, @Path("code") String code);

    @POST("api/user/complete/{id}")
    Call<String> complete(
            @Body String pass,
            @Path("id") int id);

    @GET("api/user/login/{password}/{userName}")
    Call<UserEntity> login(@Path("password") String password, @Path("userName") String userName);
}