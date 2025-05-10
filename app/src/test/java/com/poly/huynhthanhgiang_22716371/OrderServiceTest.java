package com.poly.huynhthanhgiang_22716371;

import static org.junit.Assert.assertFalse;

import android.util.Log;
import android.widget.Toast;

import com.poly.huynhthanhgiang_22716371.api.ApiService;
import com.poly.huynhthanhgiang_22716371.entity.Order;
import com.poly.huynhthanhgiang_22716371.entity.OrderItem;
import com.poly.huynhthanhgiang_22716371.entity.OrderRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderServiceTest {
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testInsertOrder() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            OrderItem orderItem = new OrderItem(i, 1, 100);
            orderItems.add(orderItem);
            total+=orderItem.getPrice();
        }

        OrderRequest orderRequest = new OrderRequest(1, orderItems, total);
        final boolean[] apiSuccess = {false};
        final Order[] createdOrder = {null};

        ApiService.apiServer.createOrder(orderRequest).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Insert order success!");
                    apiSuccess[0] = true;
                    createdOrder[0] = response.body();
                } else {
                    String errorMessage = "Lỗi khi tạo đơn hàng trên API.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " Error: " + response.code() + " " + response.errorBody().string();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        errorMessage += " Error code: " + response.code();
                    }
                    System.out.println(errorMessage);
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                System.out.println("API call failed");
                latch.countDown();
            }
        });

        latch.await();
        assertFalse("Total should match", createdOrder[0].getTotalAmount() != total);
    }

    @Test
    public void testGetOrderByUser() throws InterruptedException {
        int userId = 1;
        List<Order> orderList = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);
        final boolean[] isSuccess = {false};

        ApiService.apiServer.getUserOrders(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());

                    isSuccess[0] = true;
                    System.out.println("Tải đơn hàng thành công. Tổng số: " + orderList.size());
                } else {
                    System.out.println("Tải đơn hàng thất bại. Mã lỗi: " + response.code());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("OrderHistoryActivity", "API call failed: " + t.getMessage(), t);
                System.out.println("Lỗi tải lịch sử đơn hàng: " + t.getMessage());
                latch.countDown();
            }
        });

        latch.await();

        orderList.forEach(x -> System.out.println(x));
        assertFalse("API call should succeed", !isSuccess[0]);
        assertFalse("Order list should not be null or empty", orderList == null || orderList.isEmpty());
    }
}
