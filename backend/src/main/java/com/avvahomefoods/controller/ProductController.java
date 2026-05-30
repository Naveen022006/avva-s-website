package com.avvahomefoods.controller;

import java.util.List;
import java.util.Map;
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
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Cloudinary cloudinary;

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

    // Upload Image Endpoint — uploads to Cloudinary and returns the permanent CDN URL
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder",          "avva-home-foods",
                            "use_filename",    true,
                            "unique_filename", true,
                            "overwrite",       false
                    )
            );
            String secureUrl = (String) uploadResult.get("secure_url");
            return ResponseEntity.ok(secureUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Image upload failed: " + e.getMessage());
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

    // Delete all products (admin utility)
    @DeleteMapping("/clear-all")
    public ResponseEntity<Void> clearAllProducts() {
        productRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
