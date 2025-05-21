package com.orderservice.OrderService.orderservices;

import com.netflix.discovery.converters.Auto;
import com.orderservice.OrderService.dto.PaymentDTO;
import com.orderservice.OrderService.dto.Product;
import com.orderservice.OrderService.dto.UserDTO;
import com.orderservice.OrderService.entities.Order;
import com.orderservice.OrderService.entities.OrderItem;
import com.orderservice.OrderService.repositories.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;


@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;


    private static final String PAYMENT_SERVICE = "paymentService";

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "paymentFallback")
    public List<PaymentDTO> callPaymentService(Long orderId) {
        String url = "http://localhost:8083/Payments/order/" + orderId;

        ResponseEntity<List<PaymentDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentDTO>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Error en Payment Service: " + response.getStatusCode());
        }
    }

    public List<PaymentDTO> paymentFallback(Long orderId, Throwable t) {
        // Loguear error si quieres
        System.err.println("Payment service fallback ejecutado: " + t.getMessage());
        return List.of(); // Devuelve lista vacía como fallback

    }


    public List<Order> showAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<Order> showOrderById(Long id){
        if (orderRepository.findById(id).isPresent()){
            return orderRepository.findById(id);
        }else {
            throw new RuntimeException("Order with id: " + id + " doesn't exist");
        }
    }

    public Order createNewOrder(Order order, String authHeader) throws UnsupportedEncodingException {

        // 1. Validar token JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No se encontró el token");
        }
        String token = authHeader.substring(7);

        if (!jwtUtils.isAdmin(token)) {
            throw new RuntimeException("Acceso denegado: solo ADMIN puede acceder al UserService");
        }

        // 2. Consultar el UserService para validar que el usuario existe
        String userServiceUrl = "http://localhost:8084/admin/getuser/" + order.getOrderUserId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> userResponse;
        try {
            userResponse = restTemplate.exchange(
                    userServiceUrl,
                    HttpMethod.GET,
                    requestEntity,
                    UserDTO.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado en el UserService");
        }

        if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
            throw new RuntimeException("Usuario no válido");
        }

        // 3. Procesar los productos y calcular el total
        double total = 0.0;

        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);

            String productUrl = "http://localhost:8081/products/GetProduct/" + item.getProductName();
            String rawJson = restTemplate.getForObject(productUrl, String.class);
            System.out.println("JSON recibido: " + rawJson);

            try {
                Product product = restTemplate.getForObject(productUrl, Product.class);
                System.out.println("Producto recibido: " + product);
                System.out.println("Stock: " + product.getStock() + ", Cantidad solicitada: " + item.getQuantity());
                if (product == null) {
                    throw new RuntimeException("Producto no encontrado: " + item.getProductName());
                }
                if (product.getStock() < item.getQuantity()) {
                    throw new RuntimeException("Stock insuficiente para producto: " + item.getProductName());
                }

                total += item.getQuantity() * product.getPrice();

            } catch (HttpClientErrorException.NotFound e) {
                throw new RuntimeException("Producto no encontrado: " + item.getProductName());
            } catch (Exception ex) {
                ex.printStackTrace(); // importante para ver la excepción en consola
                throw new RuntimeException("Error al consultar el producto: " + item.getProductName() + ". Causa: " + ex.toString());
            }
        }

        // 4. Guardar la orden
        order.setTotalAmount(total);
        order.setOrderStatus("Pending");
        Order savedOrder = orderRepository.save(order);

        System.out.println("Orden guardada: " + savedOrder);
        return savedOrder;

    }

    public void updateOrderStatus(Long id,String orderStatus){
        System.out.println("El status es: " + orderStatus);
        if (orderRepository.findById(id).isPresent()){
            Order orderAux = orderRepository.findById(id).get();
            orderAux.setOrderStatus(orderStatus);
            orderRepository.save(orderAux);
        }
        else{
            throw new RuntimeException("Order with id: " + id + " doesn't exist");
        }
    }
}
