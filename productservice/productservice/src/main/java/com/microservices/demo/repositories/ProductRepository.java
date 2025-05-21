package com.microservices.demo.repositories;


import com.microservices.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    void deleteByProductName(String productName);
    Optional<Product> findByProductName(String productName);
    boolean existsByProductName(String productName);
}
