package com.poly.huynhthanhgiang_22716371.entity;

import java.util.List;

public class OrderRequest {
    private int userId;
    private List<OrderItem> items;
    private double totalAmount;

    public int getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderRequest(int userId, List<OrderItem> items, double totalAmount) {
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
    }
}