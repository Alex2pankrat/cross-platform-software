package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Role;
import com.example.demo.model.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Object findByName(String string);
    Role findByName(String name);
}
