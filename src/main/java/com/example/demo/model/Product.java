package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private Long id;
    @NotBlank
    private String title;
    private int cost;

}


