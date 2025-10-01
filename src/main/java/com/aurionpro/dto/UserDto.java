package com.aurionpro.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private Set<String> roles;
}
