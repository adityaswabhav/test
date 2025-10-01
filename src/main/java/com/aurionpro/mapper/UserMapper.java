package com.aurionpro.mapper;

import java.util.stream.Collectors;

import com.aurionpro.dto.UserDto;
import com.aurionpro.entity.Role;
import com.aurionpro.entity.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }
}
