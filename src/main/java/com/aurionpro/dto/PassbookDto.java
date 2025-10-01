package com.aurionpro.dto;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassbookDto {
    private String accountNumber;
    private BigDecimal currentBalance;
    private List<TransactionDto> transactions;
}
