package com.poly.huynhthanhgiang_22716371;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.huynhthanhgiang_22716371.adapter.ProductAdapter;
import com.poly.huynhthanhgiang_22716371.api.ApiService;
import com.poly.huynhthanhgiang_22716371.entity.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;
    AutoCompleteTextView autoSearch;
    //DBHelper dbHelper;
    DatabaseHelper dbsHelper;
    List<Product> productList;
    ProductAdapter adapter;
    Button btnCart;
    Button btnOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerProducts = findViewById(R.id.recyclerProducts);
        autoSearch = findViewById(R.id.autoSearch);
        //dbHelper = new DBHelper(this);

        btnCart = findViewById(R.id.btnCart);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);

        // Hiển thị danh sách
        productList = new ArrayList<>(); //dbHelper.getAllProducts();
        adapter = new ProductAdapter(productList, this);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerProducts.setAdapter(adapter);

        loadProductList();

        // Tìm kiếm tự động
        List<String> nameList = new ArrayList<>(); //dbHelper.getProductNames();
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, nameList);
        autoSearch.setAdapter(searchAdapter);

        autoSearch.setOnItemClickListener((parent, view, position, id) -> {
            String selected = searchAdapter.getItem(position);
            //Product found = dbHelper.findProductByName(selected);

//            if (found != null) {
//                productList.clear();
//                productList.add(found);
//                adapter.notifyDataSetChanged();
//            }
        });

        btnCart.setOnClickListener(view -> {
            Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
            startActivity(intent);
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void loadProductList() {
        ApiService.apiServer.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProductListActivity.this, "Call product list success!", Toast.LENGTH_SHORT).show();
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProductListActivity.this, "Failed to retrieve products.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Call product list failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
