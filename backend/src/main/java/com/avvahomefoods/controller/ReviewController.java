package com.avvahomefoods.controller;

import com.avvahomefoods.model.Review;
import com.avvahomefoods.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/{productId}")
    public List<Review> getReviewsByProductId(@PathVariable String productId) {
        return reviewRepository.findByProductId(productId);
    }

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        review.setCreatedAt(java.time.LocalDateTime.now());
        return reviewRepository.save(review);
    }
}
