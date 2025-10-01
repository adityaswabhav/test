package com.aurionpro.mapper;

import com.aurionpro.dto.*;
import com.aurionpro.entity.*;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerDto toDto(Customer c) {
        if (c == null) return null;
        CustomerDto dto = new CustomerDto();
        dto.setId(c.getId());
        dto.setEmailId(c.getEmailId());
        dto.setContactNo(c.getContactNo());
        dto.setDob(c.getDob());
        if (c.getAddress() != null) {
            AddressDto ad = new AddressDto(c.getAddress().getCity(), c.getAddress().getState(), c.getAddress().getPincode());
            dto.setAddress(ad);
        }
        if (c.getAccounts() != null) {
            dto.setAccounts(c.getAccounts().stream().map(AccountMapper::toDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;
        Customer c = new Customer();
        c.setEmailId(dto.getEmailId());
        c.setContactNo(dto.getContactNo());
        c.setDob(dto.getDob());
        if (dto.getAddress() != null) {
            Address addr = new Address();
            addr.setCity(dto.getAddress().getCity());
            addr.setState(dto.getAddress().getState());
            addr.setPincode(dto.getAddress().getPincode());
            c.setAddress(addr);
            if (c.getUser() != null) {
                dto.setUserId(c.getUser().getUserId());  
            }
        }
        return c;
    }
}
