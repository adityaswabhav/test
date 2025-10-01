package com.aurionpro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.dto.CustomerDto;
import com.aurionpro.dto.TransactionDto;
import com.aurionpro.service.CustomerService;
import com.aurionpro.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CustomerService customerService;
    private final TransactionService transactionService;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }


    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<List<TransactionDto>> getTransactionsForAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsForAccount(accountNumber));
    }
    
}
