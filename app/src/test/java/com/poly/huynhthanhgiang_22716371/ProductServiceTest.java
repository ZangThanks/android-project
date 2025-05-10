package com.poly.huynhthanhgiang_22716371;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.poly.huynhthanhgiang_22716371.api.ApiService;
import com.poly.huynhthanhgiang_22716371.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductServiceTest {
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testGetAllProducts() throws InterruptedException {
        List<Product> productList = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);

        ApiService.apiServer.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                System.out.println("Call product list failed!");
                latch.countDown();
            }
        });

        latch.await();
        assertFalse("List of product should not be empty", productList.isEmpty());
    }
}
