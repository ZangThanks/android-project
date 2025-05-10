package com.poly.huynhthanhgiang_22716371.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.huynhthanhgiang_22716371.R;
import com.poly.huynhthanhgiang_22716371.entity.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> list;
    private Context context;

    public ProductAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder h, int position) {
        Product p = list.get(position);
        h.tvName.setText(p.getName());
        h.tvPrice.setText("Giá: " + p.getPrice() + " VNĐ");

        h.btnAdd.setOnClickListener(v -> {
            // Thêm sản phẩm vào giỏ hàng
            CartManager.getInstance().addToCart(p);
            Toast.makeText(context, "Đã thêm " + p.getName() + " vào giỏ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}

