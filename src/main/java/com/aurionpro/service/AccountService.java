package com.aurionpro.service;

import java.util.List;

import com.aurionpro.dto.AccountDto;
import com.aurionpro.dto.PassbookDto;
import com.aurionpro.dto.TransactionDto;

public interface AccountService {
    AccountDto createAccountForLoggedInUser(AccountDto dto, String loggedInUsername);
    
    List<AccountDto> getAccountsForLoggedInUser(String loggedInUsername);
    AccountDto getAccountByNumberForLoggedInUser(String accountNumber, String loggedInUsername);
    TransactionDto performTransaction(String accountNumber, TransactionDto txDto, String loggedInUsername) throws Exception;
    PassbookDto getPassbook(String accountNumber, String loggedInUsername);
    void deleteAccount(String username, String accountNumber);
   void lockAccount(String accountNumber);
   void unlockAccount(String accountNumber);
void emailPassbookPdf(String accountNumber) throws Exception;

}
