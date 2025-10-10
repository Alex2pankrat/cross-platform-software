package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.specification.ProductSpecification;

import jakarta.annotation.PostConstruct;

@Service
@Transactional(readOnly = true)
public class ProductService {
    // private List<Product> products=new ArrayList<>(Arrays.asList(new Product(1l, "Сок", 130)));
    // private AtomicLong idGeneration = new AtomicLong(1l);
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public Product create(Product product){
        // Long id = idGeneration.getAndIncrement();
        // product.setId(id);
        // products.add(product);
        // return product;
        return productRepository.save(product);
    }
    @Cacheable(value="product", key="#id")
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getByTitle(String title){
        return productRepository.findByTitleStartingWithIgnoreCase(title);
    }
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    @Transactional
    public Product update(Long id, Product product){
        return productRepository.findById(id).map(existing -> {
            existing.setTitle(product.getTitle());
            existing.setCost(product.getCost());
            return productRepository.save(existing);
        }).orElse(null);
    }


    @CacheEvict(value = {"products", "product"}, allEntries = true)
    @Transactional
    public boolean deleteById(Long id){
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Cacheable(value = "products", key="#root.methodName")
    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Page<Product> getByFilter(String title, Integer minCost, Integer maxCost, Pageable pageable){
        return productRepository.findAll(ProductSpecification.withFilter(title,minCost,maxCost), pageable);
    }

    // @PostConstruct
    // public void init(){
    //     create(new Product(null, "ааа", 1000));
        
    // }
}

