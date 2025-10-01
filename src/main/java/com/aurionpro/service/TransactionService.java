package com.aurionpro.service;

import com.aurionpro.dto.TransactionDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<TransactionDto> getTransactionsForAccount(String accountNumber);
    List<TransactionDto> getAllTransactions();
    void deleteTransaction(Long id);

}
