package com.restaurant.backend.config;

import com.restaurant.backend.entity.Role;
import com.restaurant.backend.entity.User;
import com.restaurant.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed default admin user
        if (userRepository.findByEmail("admin@restaran.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@restaran.com");
            admin.setName("Admin");
            admin.setFullname("System Administrator");
            admin.setPasswordHash(passwordEncoder.encode("admin123")); // Default password
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin user created: admin@restaran.com / admin123");
        }
    }
}
