package com.avvahomefoods.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avvahomefoods.model.Product;
import com.avvahomefoods.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get products by category
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }

    // Search products by name
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Create a new product (admin)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Upload Image Endpoint
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            // Use Spring's resource loader for environment-agnostic path
            // For Docker/Render: Images are served from src/main/resources/static/images
            // For local dev: Can use frontend/images

            String uploadDir = "src/main/resources/static/images/";
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

            // Create directories if they don't exist (mainly for development)
            if (!java.nio.file.Files.exists(uploadPath)) {
                try {
                    java.nio.file.Files.createDirectories(uploadPath);
                } catch (Exception e) {
                    // If we can't create dirs in src/, try temp directory as fallback
                    uploadDir = System.getProperty("java.io.tmpdir") + java.io.File.separator + "avva-images"
                            + java.io.File.separator;
                    uploadPath = java.nio.file.Paths.get(uploadDir);
                    java.nio.file.Files.createDirectories(uploadPath);
                }
            }

            // Generate unique filename to avoid collisions
            String originalFilename = file.getOriginalFilename();
            String extension = ".png"; // Default
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = java.util.UUID.randomUUID().toString() + extension;
            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);

            java.nio.file.Files.write(path, file.getBytes());

            // Return relative path for frontend to access
            return ResponseEntity.ok("images/" + fileName);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Image upload failed");
        }
    }

    // Update a product (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setWeight(productDetails.getWeight());
            product.setCategory(productDetails.getCategory());
            product.setImageUrl(productDetails.getImageUrl());
            product.setInStock(productDetails.isInStock());
            product.setRating(productDetails.getRating());
            product.setReviewCount(productDetails.getReviewCount());
            product.setWeightPrices(productDetails.getWeightPrices());
            return ResponseEntity.ok(productRepository.save(product));
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a product (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
