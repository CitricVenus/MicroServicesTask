package com.orderservice.OrderService.controllers;

import com.orderservice.OrderService.dto.PaymentDTO;
import com.orderservice.OrderService.entities.Order;
import com.orderservice.OrderService.orderservices.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/Order")
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    private static final String PAYMENT_SERVICE = "paymentService";


    @GetMapping("/GetAllOrders")
    public List<Order> getAllOrders(){
        return orderService.showAllOrders();
    }

    @GetMapping("/GetOrder/{id}")
    public Optional<Order> getOrderByIs(@PathVariable Long id){
        return orderService.showOrderById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/AddOrder")
    public ResponseEntity<Order> createOrder(@RequestBody Order order,
                                             @RequestHeader("Authorization") String authHeader) throws UnsupportedEncodingException {
        Order savedOrder = orderService.createNewOrder(order, authHeader);
        return ResponseEntity.ok(savedOrder);
    }


    @PutMapping("/UpdateStatus/{status}/order/{id}")
    public void updateOrderStatus(@PathVariable Long id , @PathVariable String status){
        orderService.updateOrderStatus(id,status);
    }

    @GetMapping("/PaymentsForOrder/{orderId}")
    public List<PaymentDTO> getPaymentsForOrder(@PathVariable Long orderId) {
        return orderService.callPaymentService(orderId);
    }



}
