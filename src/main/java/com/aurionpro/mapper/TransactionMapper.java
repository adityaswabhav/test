package com.aurionpro.mapper;

import com.aurionpro.dto.TransactionDto;
import com.aurionpro.entity.Transaction;

public class TransactionMapper {

    public static TransactionDto toDto(Transaction tx) {
        if (tx == null) return null;
        return new TransactionDto(
                tx.getTransId(),
                tx.getTransType(),
                tx.getAmount(),
                tx.getDate(),
                tx.getDescription()
        );
    }
}
