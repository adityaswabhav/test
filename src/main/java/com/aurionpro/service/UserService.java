package com.aurionpro.service;
import java.util.List;

import com.aurionpro.dto.UserDto;
import com.aurionpro.entity.User;

public interface UserService {
    UserDto registerUser(String username, String password, String roleName) throws Exception;
    User findByUsername(String username);
    void deleteUser(Long id);
    List<UserDto> getAllUsers();
}
