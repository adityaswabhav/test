package com.aurionpro.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long transId;
    private String transType;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
}
