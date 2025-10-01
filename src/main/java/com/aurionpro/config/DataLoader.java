package com.aurionpro.config;

import com.aurionpro.entity.Role;
import com.aurionpro.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ADMIN"));
        }
        if (roleRepository.findByName("CUSTOMER").isEmpty()) {
            roleRepository.save(new Role(null, "CUSTOMER"));
        }
    }
}
