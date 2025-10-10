package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
public class ProductController {

    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private List<Product> products=new ArrayList<>(Arrays.asList(new Product(1l, "Сок", 130)));


    @GetMapping("/products")
    public ResponseEntity<Object> getProducts(@RequestParam(required = false) String title, 
                                        @RequestParam(required = false) Integer minCost, 
                                        @RequestParam(required = false) Integer maxCost,
                                        @PageableDefault(page=0, size=5, sort="title") Pageable pageable) {
        
        Page<Product> productPage = productService.getByFilter(title, minCost, maxCost, pageable);
        return ResponseEntity.ok(productPage);
        // if (title == null){
        //     return productService.getAll();
        // }
        // return productService.getByTitle(title);
    }
  
    
    @PostMapping("/products")
    public ResponseEntity<Product> postProducts(@RequestBody @Valid Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product!=null){
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid Product product) {
        Product edit = productService.update(id, product);
        if (edit!=null){
            return ResponseEntity.ok(edit);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        if (productService.deleteById(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
