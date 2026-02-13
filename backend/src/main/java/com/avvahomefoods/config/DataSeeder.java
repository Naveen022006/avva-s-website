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
        // Only seed if the collection is empty
        if (productRepository.count() == 0) {
            List<Product> products = Arrays.asList(
                    new Product(
                            "Sambar Powder",
                            "Traditional homemade sambar powder made with freshly roasted spices. Perfect for authentic South Indian sambar with rich aroma and flavor.",
                            149.0, "200g", "Masala Powders",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.8, 124),
                    new Product(
                            "Rasam Powder",
                            "Aromatic rasam powder blended with black pepper, cumin, and coriander. Makes the perfect tangy and spicy rasam every time.",
                            129.0, "200g", "Masala Powders",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.9, 98),
                    new Product(
                            "Chicken Masala",
                            "Fiery and flavorful chicken masala powder for restaurant-style chicken curry. A perfect blend of over 15 hand-picked spices.",
                            179.0, "200g", "Masala Powders",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.7, 156),
                    new Product(
                            "Garam Masala",
                            "Premium garam masala prepared with cinnamon, cardamom, cloves, and star anise. Adds warmth and depth to any dish.",
                            159.0, "150g", "Masala Powders",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.8, 201),
                    new Product(
                            "Fish Curry Powder",
                            "Special fish curry masala with coastal spice blend. Creates the perfect tangy and spicy fish curry with authentic taste.",
                            169.0, "200g", "Masala Powders",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.6, 87),
                    new Product(
                            "Turmeric Powder",
                            "Pure and organic turmeric powder with high curcumin content. Bright golden color and earthy aroma. No additives or coloring.",
                            99.0, "250g", "Pure Spices",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.9, 312),
                    new Product(
                            "Red Chilli Powder",
                            "Medium-hot red chilli powder made from Guntur chillies. Perfect balance of heat and color for everyday cooking.",
                            119.0, "250g", "Pure Spices",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.7, 178),
                    new Product(
                            "Coriander Powder",
                            "Freshly ground coriander powder with a citrusy aroma. Essential for everyday Indian cooking. Stone-ground for best flavor.",
                            89.0, "250g", "Pure Spices",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.8, 145),
                    new Product(
                            "Biryani Masala",
                            "Aromatic biryani masala with saffron notes. Perfect for Hyderabadi-style dum biryani. Made with 20+ premium whole spices.",
                            199.0, "150g", "Special Blends",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.9, 267),
                    new Product(
                            "Pav Bhaji Masala",
                            "Mumbai-style pav bhaji masala for street-food flavor at home. Rich blend of spices for the perfect bhaji every time.",
                            139.0, "200g", "Special Blends",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.7, 92),
                    new Product(
                            "Kulambu Chilli Powder",
                            "Traditional Tamil Nadu style kulambu chilli powder. Perfect for making aromatic and flavorful kulambu and gravies.",
                            139.0, "250g", "Masala Powders",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.8, 176),
                    new Product(
                            "Pickle Masala",
                            "Special masala blend for making traditional Indian pickles. Works great for mango, lemon, and mixed vegetable pickles.",
                            109.0, "200g", "Special Blends",
                            "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400",
                            true, 4.6, 64));

            productRepository.saveAll(products);
            System.out.println("✅ Database seeded with " + products.size() + " products!");
        } else {
            System.out.println("📦 Products already exist in database. Skipping seed.");
        }
    }
}
