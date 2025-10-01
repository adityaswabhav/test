package com.aurionpro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aurionpro.entity.Customer;
import com.aurionpro.entity.User;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUser_Username(String username);
    Optional<Customer> findByUser(User user);

    
}
