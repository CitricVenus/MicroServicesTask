package com.orderservice.OrderService.dto;

import jakarta.persistence.*;


public class PaymentDTO {

    private Long id;
    private Long orderId;
    private Double amount;
    private String paymentStatus;

    public PaymentDTO() {
    }

    public PaymentDTO(Long orderId, String paymentStatus, Double amount) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
