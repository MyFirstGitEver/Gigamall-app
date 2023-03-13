package com.example.gigamall_app.services;

import com.example.gigamall_app.tools.Tools;

import com.example.gigamall_app.dtos.CommentBoxDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CommentService {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.MINUTES)
            .build();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    CommentService service = new Retrofit.Builder()
            .baseUrl(Tools.DOMAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(CommentService.class);

    @GET("api/comments/{productId}/{page}/{ofProduct}")
    Call<List<CommentBoxDTO>> getComments(
            @Path("productId") int id, @Path("page") int page, @Path("ofProduct")boolean ofProduct);

    @GET("api/comments/replies/{replyId}/{page}/{ofProduct}")
    Call<List<CommentBoxDTO>> getReplies(
            @Path("replyId") int replyId,
            @Path("page") int page,
            @Path("ofProduct") boolean ofProduct);
}