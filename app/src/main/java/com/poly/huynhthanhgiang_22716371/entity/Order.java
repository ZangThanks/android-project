package com.poly.huynhthanhgiang_22716371.entity;

public class Order {
    private long id;
    private int productId;
    private double totalAmount;
    private String createdAt;

    public Order(long id, int productId, double totalAmount, String createdAt) {
        this.id = id;
        this.productId = productId;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productId=" + productId +
                ", totalAmount=" + totalAmount +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}