package com.aurionpro.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerDto {
    private Long id;
    private String emailId;
    private String contactNo;
    private LocalDate dob;
    private AddressDto address;
    private Long userId; 
    private List<AccountDto> accounts; 
}
