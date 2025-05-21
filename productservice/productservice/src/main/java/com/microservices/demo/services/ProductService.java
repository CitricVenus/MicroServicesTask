package com.microservices.demo.services;


import com.microservices.demo.entities.Product;
import com.microservices.demo.repositories.ProductRepository;
import org.apache.http.conn.util.PublicSuffixList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addNewProduct(Product product){
        if (!productRepository.existsByProductName(product.getProductName())){
            return productRepository.save(product);
        }
        else {
            throw new RuntimeException("Product with name: " + product.getProductName() + " already exist");
        }
    }

    public Product getProductByName(String productName) {
        // Imprimir para ver qué llega realmente
        System.out.println("Buscando producto con nombre: " + productName);

        return productRepository.findByProductName(productName)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con nombre: " + productName));
    }

    public Product updateProduct (Product product,String productName){
        if (productRepository.existsByProductName(productName)){
            Product productAux = getProductByName(productName);
            if (product.getProductName() != null) {
                productAux.setProductName(product.getProductName());
            }

            if (product.getProductDescription() != null) {
                productAux.setProductDescription(product.getProductDescription());
            }

            if (product.getProductStock() != null) {
                productAux.setProductStock(product.getProductStock());
            }

            if (product.getProductPrice() != null) {
                productAux.setProductPrice(product.getProductPrice());
            }
           return productRepository.save(productAux);

        }else{
            throw new RuntimeException("Product with name: " + productName + "doesn't exist");
        }

    }

    public String deleteProductByProductName(String productName){
        Optional<Product> productAux = productRepository.findByProductName(productName);
        if (productRepository.findById(productAux.get().getId()).isPresent()){
            productRepository.deleteById(productAux.get().getId());
            return "Product: " +productName + " deleted";
        }else{throw new RuntimeException("Product with name: " + productName + "doesn't exist");}

    }

    public Boolean checkStock(String productName){
        Optional<Product> productAux = productRepository.findByProductName(productName);
        if (productRepository.findById(productAux.get().getId()).isPresent()){
            if (productAux.get().getProductStock() > 0){
                return true;
            }else {
                return false;
            }
        }else{throw new RuntimeException("Product with name: " + productName + "doesn't exist");}
    }

    public Long getProductStockByName(String productName){
        System.out.println("Product Name: " + productName);
        if (productRepository.existsByProductName(productName)){
            if (productRepository.findByProductName(productName).get().getProductStock() > 0){
                return productRepository.findByProductName(productName).get().getProductStock() ;
            }else {
                throw new RuntimeException("No stock for: " + productName);
            }
        }else{throw new RuntimeException("Product with name: " + productName + "doesn't exist");}
    }

    public boolean checkAndReduceStock(String productName,int quantity){
        Optional<Product> productOpt = productRepository.findByProductName(productName);

        if (productOpt.isPresent()){
            Product product = productOpt.get();
            Long currentStock = product.getProductStock();
            if(currentStock >= quantity){
                product.setProductStock(currentStock-quantity);
                productRepository.save(product);
                return true;
            }

        }
            return false;

    }

}
