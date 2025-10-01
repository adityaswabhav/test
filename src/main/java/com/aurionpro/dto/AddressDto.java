package com.aurionpro.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String city;
    private String state;
    private String pincode;
}
