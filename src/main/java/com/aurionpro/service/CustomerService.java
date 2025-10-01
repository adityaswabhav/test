package com.aurionpro.service;

import com.aurionpro.dto.CustomerCreateRequest;
import com.aurionpro.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomerForLoggedInUser(CustomerCreateRequest req, String loggedInUsername);
    CustomerDto getCustomerByUsername(String username);

    List<CustomerDto> getAllCustomers();
    CustomerDto updateCustomer(String username, CustomerDto dto);
    void deleteCustomer(Long id);
}
