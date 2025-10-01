package com.aurionpro.mapper;

import com.aurionpro.dto.AccountDto;
import com.aurionpro.entity.Account;

import java.util.stream.Collectors;

public class AccountMapper {

	public static AccountDto toDto(Account acc) {
		if (acc == null)
			return null;
		AccountDto dto = new AccountDto();
		dto.setAccountId(acc.getAccountId());
		dto.setAccountNumber(acc.getAccountNumber());
		dto.setAccountType(acc.getAccountType());
		dto.setBalance(acc.getBalance());
		if (acc.getTransactions() != null) {
			dto.setTransactions(
					acc.getTransactions().stream().map(TransactionMapper::toDto).collect(Collectors.toList()));
		}
		return dto;
	}
}
