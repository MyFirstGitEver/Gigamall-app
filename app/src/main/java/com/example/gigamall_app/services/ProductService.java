package com.example.gigamall_app.services;

import com.example.gigamall_app.tools.Tools;
import com.example.gigamall_app.entities.BrandEntity;
import com.example.gigamall_app.entities.ProductEntity;
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

public interface ProductService {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.MINUTES)
            .build();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    ProductService service = new Retrofit.Builder()
            .baseUrl(Tools.DOMAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(ProductService.class);

    @GET("api/products/all")
    Call<List<ProductEntity>> getAll();

    @GET("api/products/brands/all")
    Call<List<BrandEntity>> getAlLBrands();

    @GET("api/products/category/{category}/{page}/{sortCondition}/{from}/{to}")
    Call<List<ProductEntity>> getCategorizedProducts(
            @Path("category") String category,
            @Path("page") int page,
            @Path("sortCondition") int sortCondition,
            @Path("from") float from,
            @Path("to") float to);

    @GET("api/products/category/each")
    Call<List<ProductEntity>> getEachCategory();

    @GET("api/products/mostPopular")
    Call<List<ProductEntity>> getMostPopular();

    @GET("api/products/search/{term}/{page}")
    Call<List<ProductEntity>> search(
            @Path("term") String term,
            @Path("page") int page);
}