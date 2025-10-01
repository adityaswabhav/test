package com.aurionpro.dto;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private List<TransactionDto> transactions;
}
