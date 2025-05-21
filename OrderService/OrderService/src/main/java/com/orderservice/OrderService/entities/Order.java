package com.orderservice.OrderService.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import java.util.List;

@JsonPropertyOrder({ "id", "orderUserId", "orderStatus", "totalAmount","orderItems" })
@Entity
@Table(name = "orders_table")
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column(nullable = false)
    private Long orderUserId;

    @Column(nullable = false)
    private String orderStatus = "Pending";

    @Column(nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;


    public Order() {
    }

    public Order(String orderStatus, Long orderUserId) {
        this.orderStatus = orderStatus;
        this.orderUserId = orderUserId;
    }

    public Long getId() {
        return orderId;
    }

    public Long getOrderUserId() {
        return orderUserId;
    }

    public void setId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderUserId(Long orderUserId) {
        this.orderUserId = orderUserId;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
