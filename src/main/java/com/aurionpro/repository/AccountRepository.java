package com.aurionpro.repository;

import com.aurionpro.entity.Account;
import com.aurionpro.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomer(Customer customer);
}
