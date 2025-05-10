package com.poly.huynhthanhgiang_22716371.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poly.huynhthanhgiang_22716371.entity.LoginRequest;
import com.poly.huynhthanhgiang_22716371.entity.LoginResponse;
import com.poly.huynhthanhgiang_22716371.entity.Order;
import com.poly.huynhthanhgiang_22716371.entity.OrderRequest;
import com.poly.huynhthanhgiang_22716371.entity.Product;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
    String BASE_URL = "https://680b384ad5075a76d98a3eca.mockapi.io/";
    ApiService apiServer = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("/api/v1/Product")
    Call<List<Product>> getAllProducts();

    @GET("/api/v1/Product/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @POST("/api/v1/Order")
    Call<Order> createOrder(@Body OrderRequest request);

    @GET("api/v1/Order")
    Call<List<Order>> getUserOrders(@Query("userId") int userId);
}
