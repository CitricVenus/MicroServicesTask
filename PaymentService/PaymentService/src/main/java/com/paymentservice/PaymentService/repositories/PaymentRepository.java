package com.paymentservice.PaymentService.repositories;


import com.paymentservice.PaymentService.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByOrderId(Long orderId);
}
