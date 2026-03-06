package com.avvahomefoods.config;

import com.avvahomefoods.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

        @Autowired
        private ProductRepository productRepository;

        @Override
        public void run(String... args) {
                long count = productRepository.count();
                System.out.println("✅ DataSeeder: " + count
                                + " product(s) in database. Add/manage products via the Admin Dashboard.");
        }

}
