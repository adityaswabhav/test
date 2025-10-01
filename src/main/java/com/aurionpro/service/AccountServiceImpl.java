package com.aurionpro.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.dto.AccountDto;
import com.aurionpro.dto.PassbookDto;
import com.aurionpro.dto.TransactionDto;
import com.aurionpro.email.EmailService;
import com.aurionpro.entity.Account;
import com.aurionpro.entity.Customer;
import com.aurionpro.entity.Transaction;
import com.aurionpro.mapper.AccountMapper;
import com.aurionpro.mapper.TransactionMapper;
import com.aurionpro.repository.AccountRepository;
import com.aurionpro.repository.CustomerRepository;
import com.aurionpro.repository.TransactionRepository;
import com.aurionpro.repository.UserRepository;
import com.aurionpro.util.PdfGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AccountDto createAccountForLoggedInUser(AccountDto dto, String loggedInUsername) {
        Customer customer = customerRepository.findByUser_Username(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found. Create profile first."));

        Account acc = new Account();
        acc.setAccountNumber(generateUniqueAccountNumber());  
        acc.setAccountType(dto.getAccountType());
        acc.setBalance(dto.getBalance() == null ? BigDecimal.ZERO : dto.getBalance());
        acc.setCustomer(customer);

        Account saved = accountRepository.save(acc);

        emailService.sendSimpleEmail(
                customer.getEmailId(),
                "Account Created Successfully",
                "Dear " + customer.getEmailId() + ",\n\nYour account (" + saved.getAccountNumber() +
                        ") has been created successfully with balance: " + saved.getBalance()
        );

        return AccountMapper.toDto(saved);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "ACC" + (10000000 + new Random().nextInt(90000000)); 
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }


    @Override
    public List<AccountDto> getAccountsForLoggedInUser(String loggedInUsername) {
        Customer c = customerRepository.findByUser_Username(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found"));
        return accountRepository.findByCustomer(c).stream().map(AccountMapper::toDto).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteAccount(String username, String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!account.getCustomer().getUser().getUsername().equals(username)) {
            throw new SecurityException("You can only delete your own account");
        }
        accountRepository.delete(account);
    }

    @Override
    public AccountDto getAccountByNumberForLoggedInUser(String accountNumber, String loggedInUsername) {
        Account a = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!a.getCustomer().getUser().getUsername().equals(loggedInUsername)) {
            throw new SecurityException("You cannot view another user's account");
        }
        return AccountMapper.toDto(a);
    }

    @Override
    @Transactional
    public TransactionDto performTransaction(String accountNumber, TransactionDto txDto, String loggedInUsername) throws Exception {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getCustomer().getUser().getUsername().equals(loggedInUsername)) {
            throw new SecurityException("You cannot perform transactions on someone else’s account");
        }
        
        if (!account.isActive()) {
            throw new SecurityException("This account is locked. No transactions allowed.");
        }


        BigDecimal amt = txDto.getAmount();
        if (amt == null || amt.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Transaction tx = new Transaction();
        tx.setTransType(txDto.getTransType());
        tx.setAmount(amt);
        tx.setDate(LocalDateTime.now());
        tx.setDescription(txDto.getDescription());
        tx.setAccount(account);

        if ("debit".equalsIgnoreCase(txDto.getTransType())) {
            if (account.getBalance().compareTo(amt) < 0) throw new IllegalArgumentException("Insufficient balance");
            account.setBalance(account.getBalance().subtract(amt));
        } else if ("credit".equalsIgnoreCase(txDto.getTransType())) {
            account.setBalance(account.getBalance().add(amt));
        } else if ("transfer".equalsIgnoreCase(txDto.getTransType())) {
            String targetAccNumber = txDto.getDescription();
            if (targetAccNumber == null) throw new IllegalArgumentException("Target account required");
            Account target = accountRepository.findByAccountNumber(targetAccNumber)
                    .orElseThrow(() -> new IllegalArgumentException("Target account not found"));
            if (account.getBalance().compareTo(amt) < 0) throw new IllegalArgumentException("Insufficient balance");
            account.setBalance(account.getBalance().subtract(amt));
            target.setBalance(target.getBalance().add(amt));
            accountRepository.save(target);
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        Transaction savedTx = transactionRepository.save(tx);
        account.getTransactions().add(savedTx);
        accountRepository.save(account);

        // email
        emailService.sendSimpleEmail(
                account.getCustomer().getEmailId(),
                "Transaction Alert",
                "Dear " + account.getCustomer().getEmailId() + ",\n\nYour account (" +
                        account.getAccountNumber() + ") has a " + tx.getTransType().toUpperCase() +
                        " of amount: " + amt + ".\nAvailable balance: " + account.getBalance()
        );

        return TransactionMapper.toDto(savedTx);
    }

    @Override
    public PassbookDto getPassbook(String accountNumber, String loggedInUsername) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!acc.getCustomer().getUser().getUsername().equals(loggedInUsername)) {
            throw new SecurityException("You cannot view other customers' passbook");
        }

        PassbookDto pb = new PassbookDto();
        pb.setAccountNumber(acc.getAccountNumber());
        pb.setCurrentBalance(acc.getBalance());
        pb.setTransactions(acc.getTransactions().stream().map(TransactionMapper::toDto).collect(Collectors.toList()));
        return pb;
    }
    
    @Override
    public void lockAccount(String accountNumber) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        acc.setActive(false);
        accountRepository.save(acc);
    }

    @Override
    public void unlockAccount(String accountNumber) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        acc.setActive(true);
        accountRepository.save(acc);
    }
    
    @Override
    public void emailPassbookPdf(String accountNumber) throws Exception {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        byte[] pdfBytes = PdfGenerator.generatePassbookPdf(accountNumber, acc.getTransactions());

        emailService.sendEmailWithAttachment(
                acc.getCustomer().getEmailId(),
                "Passbook PDF",
                "Dear " + acc.getCustomer().getEmailId() + ",\n\nPlease find attached your passbook statement.",
                pdfBytes,
                "passbook.pdf"
        );
    }



}
