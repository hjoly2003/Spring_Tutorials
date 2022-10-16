package com.circuitbreaker.model;

public class Product {
    int productId;
    String title;
    int quantity;

    public Product() {
    }

    public Product(int productId, String title, int quantity) {
        this.productId = productId;
        this.title = title;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", title='" + title + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}