package com.avvahomefoods.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.avvahomefoods.model.User;
import com.avvahomefoods.repository.UserRepository;

@Configuration
public class AdminSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedAdmin() {
        return args -> {
            try {
                // Check if admin exists
                if (!userRepository.existsByUsername("admin")) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder.encode("admin123")); // Default password
                    admin.setName("System Admin");
                    admin.setRole("ADMIN");

                    userRepository.save(admin);
                    System.out.println(">>> REF-LOG: Default Admin User Created (admin / admin123) <<<");
                } else {
                    System.out.println(">>> REF-LOG: Admin User already exists. Skipping seed. <<<");
                }
            } catch (Exception e) {
                System.err.println(">>> REF-LOG: AdminSeeder failed — MongoDB may be unreachable: " + e.getMessage() + " <<<");
                // Do NOT rethrow — app must not crash if seeding fails
            }
        };
    }
}
