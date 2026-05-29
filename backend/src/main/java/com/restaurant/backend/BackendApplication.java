package com.restaurant.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public org.springframework.boot.CommandLineRunner initData(
            com.restaurant.backend.repository.UserRepository userRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@restaran.com").isEmpty()) {
                com.restaurant.backend.entity.User admin = new com.restaurant.backend.entity.User();
                admin.setName("admin");
                admin.setFullname("System Administrator");
                admin.setEmail("admin@restaran.com");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(com.restaurant.backend.entity.Role.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
