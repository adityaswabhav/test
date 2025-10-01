package com.aurionpro.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.dto.AccountDto;
import com.aurionpro.dto.PassbookDto;
import com.aurionpro.dto.TransactionDto;
import com.aurionpro.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto dto,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        AccountDto createdAccount = accountService.createAccountForLoggedInUser(dto, userDetails.getUsername());
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/my")
    public ResponseEntity<List<AccountDto>> myAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getAccountsForLoggedInUser(userDetails.getUsername()));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable String accountNumber,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getAccountByNumberForLoggedInUser(accountNumber, userDetails.getUsername()));
    }
    

    @PostMapping("/{accountNumber}/transaction")
    public ResponseEntity<TransactionDto> transact(@PathVariable String accountNumber,
                                                   @RequestBody TransactionDto dto,
                                                   @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        return ResponseEntity.ok(accountService.performTransaction(accountNumber, dto, userDetails.getUsername()));
    }

    @GetMapping("/{accountNumber}/passbook")
    public ResponseEntity<PassbookDto> passbook(@PathVariable String accountNumber,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getPassbook(accountNumber, userDetails.getUsername()));
    }
    
    @DeleteMapping("/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        accountService.deleteAccount(userDetails.getUsername(), accountNumber);
        return ResponseEntity.ok("Account deleted successfully");
    }
    @PatchMapping("{accountNumber}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> lockAccount(@PathVariable String accountNumber) {
        accountService.lockAccount(accountNumber);
        return ResponseEntity.ok("Account locked successfully.");
    }

    @PatchMapping("/{accountNumber}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> unlockAccount(@PathVariable String accountNumber) {
        accountService.unlockAccount(accountNumber);
        return ResponseEntity.ok("Account unlocked successfully.");
    }
    @GetMapping("/{accountNumber}/passbook/pdf")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> emailPassbook(@PathVariable String accountNumber) throws Exception {
        accountService.emailPassbookPdf(accountNumber);
        return ResponseEntity.ok("Passbook PDF emailed successfully.");
    }

    

}
