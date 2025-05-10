package com.poly.huynhthanhgiang_22716371.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.huynhthanhgiang_22716371.R;
import com.poly.huynhthanhgiang_22716371.entity.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartList;
    private Context context;

    public CartAdapter(List<CartItem> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder h, int pos) {
        CartItem item = cartList.get(pos);
        h.tvName.setText(item.getProduct().getName());
        h.tvPrice.setText("Giá: " + item.getProduct().getPrice());
        h.tvQuantity.setText("Số lượng: " + item.getQuantity());

        h.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyDataSetChanged();
        });

        h.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyDataSetChanged();
            }
        });

        h.btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(item.getProduct().getId());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        Button btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}

