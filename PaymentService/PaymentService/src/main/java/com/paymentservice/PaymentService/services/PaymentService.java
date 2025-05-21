package com.paymentservice.PaymentService.services;

import com.paymentservice.PaymentService.dto.Order;
import com.paymentservice.PaymentService.dto.PaymentRequest;
import com.paymentservice.PaymentService.dto.UserDTO;
import com.paymentservice.PaymentService.repositories.PaymentRepository;
import com.paymentservice.PaymentService.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.paymentservice.PaymentService.entities.Payment;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    JwtUtils jwtUtils;


    public ResponseEntity<String> processPayment(PaymentRequest request, String authHeader) {

        // 1. Validar token JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No se encontró el token");
        }
        String token = authHeader.substring(7);

        if (!jwtUtils.isAdmin(token)) {
            throw new RuntimeException("Acceso denegado: solo ADMIN puede acceder al UserService");
        }

        // Prepara headers con el token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // 2. Obtener la orden desde OrderService con token
        String orderServiceUrl = "http://localhost:8082/Order/GetOrder/" + request.getOrderId();

        ResponseEntity<Order> orderResponse;
        try {
            orderResponse = restTemplate.exchange(
                    orderServiceUrl,
                    HttpMethod.GET,
                    requestEntity,
                    Order.class
            );
        } catch (HttpClientErrorException.Forbidden e) {
            throw new RuntimeException("Acceso denegado al OrderService: " + e.getMessage());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden no encontrada");
        }

        if (!orderResponse.getStatusCode().is2xxSuccessful() || orderResponse.getBody() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden no encontrada");
        }

        Order order = orderResponse.getBody();

        // 3. Validar monto

        System.out.println(order.getTotalAmount());
        System.out.println(request.getAmount());

        if (!Objects.equals(order.getTotalAmount(), request.getAmount())) {
            return ResponseEntity.badRequest().body("Monto incorrecto");
        }

        // 4. Actualizar status de la orden a PAID enviando token
        String updateStatusUrl = "http://localhost:8082/Order/UpdateStatus/PAID/order/" + request.getOrderId();
        HttpEntity<Void> putRequestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                updateStatusUrl,
                HttpMethod.PUT,
                putRequestEntity,
                Void.class
        );

        // 5. Guardar pago localmente
        Payment payment = new Payment(request.getOrderId(), "PAID", request.getAmount());
        paymentRepository.save(payment);

        return ResponseEntity.ok("Pago registrado correctamente");
    }


        public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
