package com.aurionpro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aurionpro.dto.TransactionDto;
import com.aurionpro.entity.Account;
import com.aurionpro.mapper.TransactionMapper;
import com.aurionpro.repository.AccountRepository;
import com.aurionpro.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
	private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;  


	@Override
	public List<TransactionDto> getTransactionsForAccount(String accountNumber) {
		Account acc = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));
		return acc.getTransactions().stream().map(TransactionMapper::toDto).collect(Collectors.toList());
	}
	
	@Override
    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream().map(TransactionMapper::toDto).toList();
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
    
    
}
