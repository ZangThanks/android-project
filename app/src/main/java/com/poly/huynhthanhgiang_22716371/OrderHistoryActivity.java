package com.poly.huynhthanhgiang_22716371;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.huynhthanhgiang_22716371.adapter.OrderAdapter;
import com.poly.huynhthanhgiang_22716371.api.ApiService;
import com.poly.huynhthanhgiang_22716371.entity.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private OrderAdapter adapter;
    // private DBHelper dbHelper;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử đơn hàng");
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerOrders = findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList);
        recyclerOrders.setAdapter(adapter);

        // dbHelper = new DBHelper(this);
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        //SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = 1;//prefs.getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService.apiServer.getUserOrders(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (orderList.isEmpty()) {
                        Toast.makeText(OrderHistoryActivity.this, "Không có đơn hàng nào.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Failed to retrieve order history.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " Error: " + response.code() + " " + response.errorBody().string();
                        } catch (IOException e) {
                            Log.e("OrderHistoryActivity", "Error parsing errorBody", e);
                        }
                    } else {
                        errorMessage += " Error code: " + response.code();
                    }
                    Log.e("OrderHistoryActivity", errorMessage);
                    Toast.makeText(OrderHistoryActivity.this, "Failed to load order history. Code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("OrderHistoryActivity", "API call failed: " + t.getMessage(), t);
                Toast.makeText(OrderHistoryActivity.this, "Lỗi tải lịch sử đơn hàng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}