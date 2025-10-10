package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.model.Product;

public class ProductSpecification {
    private static Specification<Product> titleLike(String title){
        return (root, query, criteriaBuilder) -> {
            if ( title == null || title.trim().isEmpty() ){
                return null;
            }
            return criteriaBuilder.like( criteriaBuilder.lower( root.get("title") ), "%"+title.toLowerCase().trim()+"%" );
        };
    }
    private static Specification<Product> costBetween(Integer minCost, Integer maxCost){
        return (root, query, criteriaBuilder) -> {
            if (minCost==null && maxCost==null) { return null; }
            if (minCost!= null && maxCost != null) { return criteriaBuilder.between(root.get("cost"),minCost,maxCost);}
            if (minCost!= null){ return criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minCost); }
            return criteriaBuilder.lessThanOrEqualTo(root.get("cost"),maxCost);
        };
    }
    public static Specification<Product> withFilter(String title, Integer minCost, Integer maxCost){
        return Specification.allOf(titleLike(title), costBetween(minCost, maxCost));
    }
}
