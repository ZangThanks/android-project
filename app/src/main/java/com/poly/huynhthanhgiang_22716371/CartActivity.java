package com.poly.huynhthanhgiang_22716371;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.huynhthanhgiang_22716371.adapter.CartAdapter;
import com.poly.huynhthanhgiang_22716371.adapter.CartManager;
import com.poly.huynhthanhgiang_22716371.api.ApiService;
import com.poly.huynhthanhgiang_22716371.entity.CartItem;
import com.poly.huynhthanhgiang_22716371.entity.Order;
import com.poly.huynhthanhgiang_22716371.entity.OrderItem;
import com.poly.huynhthanhgiang_22716371.entity.OrderRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerCart;
    TextView tvTotal;
    Button btnCheckout;
    CartAdapter adapter;
    List<CartItem> cartList;
    // DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Giỏ hàng");
        }

        recyclerCart = findViewById(R.id.recyclerCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        // dbHelper = new DBHelper(this);
        cartList = CartManager.getInstance().getCartList();
        adapter = new CartAdapter(cartList, this);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(adapter);

        updateTotal();

        btnCheckout.setOnClickListener(v -> {
            double total = CartManager.getInstance().getTotalAmount();
            if (total == 0) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            //int userId = prefs.getInt("user_id", -1); // Retrieve userId, default to -1 if not found
            int userId = 1;

            if (userId == -1) {
                Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
                return;
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : CartManager.getInstance().getCartList()) {
                orderItems.add(new OrderItem(
                        cartItem.getProduct().getId(),
                        cartItem.getQuantity(),
                        cartItem.getProduct().getPrice()
                ));
            }

            OrderRequest orderRequest = new OrderRequest(userId, orderItems, total);

            ApiService.apiServer.createOrder(orderRequest).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(CartActivity.this, "Đặt hàng thành công! Tổng tiền: " + total + " VNĐ", Toast.LENGTH_LONG).show();
                        CartManager.getInstance().clearCart();
                        // adapter.notifyDataSetChanged();
                        updateCartDisplay();
                    } else {
                        String errorMessage = "Lỗi khi tạo đơn hàng trên API.";
                        if (response.errorBody() != null) {
                            try {
                                errorMessage += " Error: " + response.code() + " " + response.errorBody().string();
                            } catch (Exception e) {
                                Log.e("CartActivity", "Error parsing errorBody", e);
                            }
                        } else {
                            errorMessage += " Error code: " + response.code();
                        }
                        Log.e("CartActivity", errorMessage);
                        Toast.makeText(CartActivity.this, "Lỗi khi tạo đơn hàng. Code: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Log.e("CartActivity", "API call failed: " + t.getMessage(), t);
                    Toast.makeText(CartActivity.this, "Đặt hàng thất bại: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTotal() {
        tvTotal.setText("Tổng tiền: " + CartManager.getInstance().getTotalAmount() + " VNĐ");
    }

    private void updateCartDisplay() {
        // cartList = CartManager.getInstance().getCartList(); // cartList is already a reference
        if (adapter != null) { // Add null check for adapter
            adapter.notifyDataSetChanged();
        }
        updateTotal();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartDisplay();
    }

    // These methods are no longer needed as we are using API
    // private long saveOrderToDatabase(double totalAmount) {
    //     int customerId = 1;  // Giả sử customerId = 1 (hoặc lấy từ thông tin người dùng)
    //     return dbHelper.insertOrder(customerId, totalAmount);
    // }

    // private void saveOrderItemsToDatabase(long orderId) {
    //     List<CartItem> cartItems = CartManager.getInstance().getCartList();
    //     for (CartItem item : cartItems) {
    //         dbHelper.insertOrderItems(orderId, item.getProduct().getId(), item.getQuantity(), item.getTotalPrice());
    //     }
    // }
}

