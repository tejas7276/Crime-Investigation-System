package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    // ✨ New — find users by role
    List<User> findByRole(String role); // This method remains unchanged

    // If you expect only one admin, modify the method:
    // Optional<User> findFirstByRole(String role); 
}
