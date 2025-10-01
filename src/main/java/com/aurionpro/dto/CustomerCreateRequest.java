package com.aurionpro.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerCreateRequest {
    private String emailId;
    private String contactNo;
    private LocalDate dob;
    public com.aurionpro.dto.AddressDto address;
}
