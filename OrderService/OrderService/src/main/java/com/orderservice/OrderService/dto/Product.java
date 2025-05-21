package com.orderservice.OrderService.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

public class Product {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productDescription")
    private  String productDescription;
    @JsonProperty("productPrice")
    private Long productPrice;
    @JsonProperty("productStock")
    private Long productStock;

    // Getters y Setters
    public Product() {

    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return productPrice;
    }

    public Long getStock() {
        return productStock;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Long price) {
        this.productPrice = price;
    }

    public void setStock(Long stock) {
        this.productStock = stock;
    }
}