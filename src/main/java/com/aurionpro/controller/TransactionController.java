package com.aurionpro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.dto.TransactionDto;
import com.aurionpro.service.AccountService;
import com.aurionpro.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionDto>> getTransactionsForAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsForAccount(accountNumber));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }
    
    
    @PostMapping("/perform/{accountNumber}")
    public TransactionDto performTransaction(
            @PathVariable String accountNumber,
            @RequestBody TransactionDto txDto,
            Authentication auth
    ) throws Exception {
        String username = auth.getName();
        return accountService.performTransaction(accountNumber, txDto, username);
    }
    
    

}
