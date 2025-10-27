package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import com.example.demo.enums.TokenType;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TokenType type;
    private String value;
    private LocalDateTime expiryDate;
    private boolean disabled;
    @ManyToOne
    private User user;
    public Token(TokenType type, String value, LocalDateTime expiryDate, boolean disabled, User user) {
        this.type = type;
        this.value = value;
        this.expiryDate = expiryDate;
        this.disabled = disabled;
        this.user = user;
    }
}
