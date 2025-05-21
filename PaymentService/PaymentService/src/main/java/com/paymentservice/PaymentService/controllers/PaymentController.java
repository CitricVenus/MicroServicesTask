package com.paymentservice.PaymentService.controllers;


import com.paymentservice.PaymentService.dto.PaymentRequest;
import com.paymentservice.PaymentService.entities.Payment;
import com.paymentservice.PaymentService.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/pay")
    public ResponseEntity<String> payForOrder(@RequestBody PaymentRequest request,@RequestHeader("Authorization") String authHeader) {
        return paymentService.processPayment(request,authHeader);
    }

    @GetMapping("/order/{orderId}")
    public List<Payment> getPaymentsByOrderId(@PathVariable Long orderId) {
        return paymentService.getPaymentsByOrderId(orderId);
    }
}
