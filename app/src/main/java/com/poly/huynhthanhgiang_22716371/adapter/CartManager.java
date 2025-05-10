package com.poly.huynhthanhgiang_22716371.adapter;

import com.poly.huynhthanhgiang_22716371.entity.CartItem;
import com.poly.huynhthanhgiang_22716371.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartList;

    private CartManager() {
        cartList = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product p) {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        for (CartItem item : cartList) {
            if (item.getProduct().getId() == p.getId()) {
                // Nếu có rồi thì tăng số lượng
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        // Nếu chưa có thì thêm mới với số lượng là 1
        cartList.add(new CartItem(p, 1));
    }

    public void removeFromCart(int productId) {
        cartList.removeIf(item -> item.getProduct().getId() == productId);
    }

    public List<CartItem> getCartList() {
        return cartList;
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        cartList.clear();
    }
}

