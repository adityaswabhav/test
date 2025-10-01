package com.aurionpro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aurionpro.dto.CustomerCreateRequest;
import com.aurionpro.dto.CustomerDto;
import com.aurionpro.entity.Address;
import com.aurionpro.entity.Customer;
import com.aurionpro.entity.User;
import com.aurionpro.mapper.CustomerMapper;
import com.aurionpro.repository.CustomerRepository;
import com.aurionpro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public CustomerDto createCustomerForLoggedInUser(CustomerCreateRequest req, String loggedInUsername) {
        User user = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (customerRepository.findByUser_Username(loggedInUsername).isPresent()) {
            throw new IllegalArgumentException("Customer profile already exists for this user");
        }

        Customer c = new Customer();
        c.setEmailId(req.getEmailId());
        c.setContactNo(req.getContactNo());
        c.setDob(req.getDob());

        if (req.getAddress() != null) {
            Address addr = new Address();
            addr.setCity(req.getAddress().getCity());
            addr.setState(req.getAddress().getState());
            addr.setPincode(req.getAddress().getPincode());
            c.setAddress(addr);
        }

        c.setUser(user);

        Customer saved = customerRepository.save(c);
        return CustomerMapper.toDto(saved);
    }

    @Override
    public CustomerDto getCustomerByUsername(String username) {
        Customer c = customerRepository.findByUser_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found for user"));
        return CustomerMapper.toDto(c);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public CustomerDto updateCustomer(String username, CustomerDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
        customer.setContactNo(dto.getContactNo());
        customer.setDob(dto.getDob());
        customer.setEmailId(dto.getEmailId());
        return CustomerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
