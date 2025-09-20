package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class ProductController {
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getAll();
    }
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    private List<Product> products=new ArrayList<>(Arrays.asList(new Product(1l, "Сок", 130)));
    
    @PostMapping("/products")
    public ResponseEntity<Product> postProducts(@RequestBody @Valid Product product) {
        //TODO: process POST request
        // product.setId((long)products.size()+1);
        // products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        for (Product product : products) {
            if(product.getId().equals(id)){
                return ResponseEntity.ok(product);
            }
        }
        return ResponseEntity.notFound().build();
        // return new String();
    }
    
    
}