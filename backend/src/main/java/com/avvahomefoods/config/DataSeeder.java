package com.avvahomefoods.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.avvahomefoods.model.Product;
import com.avvahomefoods.repository.ProductRepository;
import com.avvahomefoods.repository.StoreSettingsRepository;

@Component
public class DataSeeder implements CommandLineRunner {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private StoreSettingsRepository storeSettingsRepository;

        @Override
        public void run(String... args) {
                try {
                        if (productRepository.count() > 0) {
                                System.out.println("✅ DataSeeder: products already exist, skipping seed.");
                                return;
                        }

                        List<Product> products = Arrays.asList(
                                createProduct("Sambar Powder",
                                        "Aromatic homemade sambar powder made from roasted lentils, dried red chillies, coriander, cumin and curry leaves. Adds a rich tangy depth to any sambar or kootu.",
                                        120.0, "100g", "Masala Powders", "", true, 4.8, 124,
                                        "Coriander, Cumin, Red Chillies, Black Pepper, Chana Dal, Urad Dal, Curry Leaves, Turmeric"),
                                createProduct("Rasam Powder",
                                        "Traditional South Indian rasam powder with a perfect blend of pepper, cumin, coriander and dried red chillies. Makes a soul-warming rasam in minutes.",
                                        100.0, "100g", "Masala Powders", "", true, 4.7, 98,
                                        "Black Pepper, Cumin, Coriander, Red Chillies, Turmeric, Toor Dal"),
                                createProduct("Chettinad Masala",
                                        "Bold and fiery Chettinad masala featuring marathi mokku, kalpasi and star anise. Perfect for authentic Chettinad chicken, mutton and vegetable curries.",
                                        150.0, "100g", "Special Blends", "", true, 4.9, 76,
                                        "Star Anise, Kalpasi, Marathi Mokku, Cinnamon, Cloves, Cardamom, Red Chillies, Coriander"),
                                createProduct("Idli Milagai Podi",
                                        "Classic gunpowder — spicy, garlicky and nutty. A must-have side for idli, dosa and uttapam. Mix with sesame oil for an irresistible dip.",
                                        90.0, "150g", "Masala Powders", "", true, 4.6, 211,
                                        "Urad Dal, Chana Dal, Red Chillies, Sesame Seeds, Garlic, Curry Leaves, Salt"),
                                createProduct("Turmeric Powder",
                                        "Pure, sun-dried and stone-ground turmeric from Erode farms. Deep golden colour, rich curcumin content and a mild earthy flavour.",
                                        80.0, "100g", "Pure Spices", "", true, 4.5, 53,
                                        "100% Pure Turmeric"),
                                createProduct("Biryani Masala",
                                        "Fragrant whole-spice biryani masala blend that transforms every pot of rice into a restaurant-quality dum biryani. No artificial colours or preservatives.",
                                        130.0, "100g", "Special Blends", "", true, 4.7, 88,
                                        "Bay Leaves, Cardamom, Cloves, Cinnamon, Star Anise, Mace, Nutmeg, Black Pepper, Cumin, Coriander")
                        );

                        productRepository.saveAll(products);
                        System.out.println("✅ DataSeeder: seeded " + products.size() + " sample products.");
                } catch (Exception e) {
                        System.err.println("⚠️ DataSeeder failed (non-fatal): " + e.getMessage());
                }
        }

        private Product createProduct(String name, String description, double price,
                        String weight, String category, String imageUrl,
                        boolean inStock, double rating, int reviewCount, String ingredients) {
                Product p = new Product(name, description, price, weight, category, imageUrl, inStock, rating, reviewCount, ingredients);
                java.util.Map<String, Double> weightPrices = new java.util.LinkedHashMap<>();
                weightPrices.put("100g", price);
                weightPrices.put("250g", price * 2.3);
                weightPrices.put("500g", price * 4.2);
                p.setWeightPrices(weightPrices);
                return p;
        }

}
