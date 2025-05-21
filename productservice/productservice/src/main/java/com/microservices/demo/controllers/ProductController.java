package com.microservices.demo.controllers;


import com.microservices.demo.entities.Product;
import com.microservices.demo.services.ProductService;
import org.hibernate.annotations.DialectOverride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/GetProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/GetProduct/{productName}")
    public Product getProductByProductName(@PathVariable String productName){
        return productService.getProductByName(productName);
    }

    @PostMapping("/AddProduct")
    public Product addNewProduct(@RequestBody Product product){
        return productService.addNewProduct(product);
    }

    @PostMapping("/UpdateProduct/{productName}")
    public Product updateProduct (@RequestBody Product product , String productName){
        return productService.updateProduct(product,productName);
    }

    @DeleteMapping("/DeleteProduct/{productName}")
    public String deleteProductByProductName(@PathVariable String productName){
        return productService.deleteProductByProductName(productName);
    }

    @GetMapping("/GetStock/{productName}")
    public Long getStockByProdcutName(@PathVariable String productName){
        return productService.getProductStockByName(productName);
    }

    @GetMapping("/CheckStockAndReduce/{productName}/{quantity}")
    public boolean checkStock(@PathVariable String productName, @PathVariable int quantity) {
        return productService.checkAndReduceStock(productName, quantity);
    }





}
