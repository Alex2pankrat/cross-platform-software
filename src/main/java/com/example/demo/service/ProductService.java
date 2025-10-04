package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService {
    // private List<Product> products=new ArrayList<>(Arrays.asList(new Product(1l, "Сок", 130)));
    // private AtomicLong idGeneration = new AtomicLong(1l);
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product){
        // Long id = idGeneration.getAndIncrement();
        // product.setId(id);
        // products.add(product);
        // return product;
        return productRepository.save(product);
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getByTitle(String title){
        return productRepository.findByTitleStartingWithIgnoreCase(title);
    }

    public Product update(Long id, Product product){
        return productRepository.findById(id).map(existing -> {
            existing.setTitle(product.getTitle());
            existing.setCost(product.getCost());
            return productRepository.save(existing);
        }).orElse(null);
    }

    public boolean deleteById(Long id){
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    @PostConstruct
    public void init(){
        create(new Product(null, "Кефир", 123));
        
    }
}

