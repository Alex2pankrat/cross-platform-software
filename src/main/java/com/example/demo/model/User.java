package com.example.demo.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ManyToOne
    private Role role;

    @OneToMany
    private Set<Token> tokens;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> authorities = new HashSet<>();
        role.getPermissions().forEach(p -> authorities.add(p.getAuthority()));
        authorities.add(role.getAuthority());
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

}
