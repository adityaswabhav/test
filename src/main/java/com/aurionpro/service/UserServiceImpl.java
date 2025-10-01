package com.aurionpro.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.dto.UserDto;
import com.aurionpro.entity.Role;
import com.aurionpro.entity.User;
import com.aurionpro.mapper.UserMapper;
import com.aurionpro.repository.RoleRepository;
import com.aurionpro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(String username, String password, String role) throws Exception {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        String finalRole = (role == null || role.isBlank()) ? "CUSTOMER" : role.toUpperCase();

        Role userRole = roleRepository.findByName(finalRole)
                .orElseThrow(() -> new RuntimeException("Role not found: " + finalRole));

        user.setRoles(Collections.singleton(userRole));

        User saved = userRepository.save(user);

        UserDto dto = new UserDto();
        dto.setUserId(saved.getUserId());
        dto.setUsername(saved.getUsername());
        dto.setRoles(saved.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }
    
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
