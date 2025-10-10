package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByTitleStartingWithIgnoreCase(String title);
    @Query(value="SELECT p FROM shop WHERE p.cost=(SELECT MAX(cost) FROM shop)", nativeQuery = true)
    List<Product> findMostExpensive();
    @Query(value="SELECT p FROM shop p WHERE p.cost BETWEEN :minCost AND :maxCost ORDER BY p.cost DESC", nativeQuery = true)
    List<Product> findCostBetween(@Param("minCost") int minCost, @Param("maxCost") int maxCost);


}
