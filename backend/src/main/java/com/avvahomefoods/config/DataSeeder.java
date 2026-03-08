package com.avvahomefoods.config;

import com.avvahomefoods.model.Product;
import com.avvahomefoods.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

        @Autowired
        private ProductRepository productRepository;

        @Override
        public void run(String... args) {
                // Dummy products have been removed so you can add your own via the Admin Panel.
                System.out.println("✅ DataSeeder running: no dummy products seeded.");
        }

}
