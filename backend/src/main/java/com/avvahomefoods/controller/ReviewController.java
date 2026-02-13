package com.avvahomefoods.controller;

import com.avvahomefoods.model.Review;
import com.avvahomefoods.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/top")
    public List<Review> getTopReviews() {
        // Try to get 5-star reviews first
        List<Review> topReviews = reviewRepository.findTop3ByRatingOrderByCreatedAtDesc(5);

        // If not enough, fill with recent reviews
        if (topReviews.size() < 3) {
            List<Review> recent = reviewRepository.findAllByOrderByCreatedAtDesc();
            // Simple logic: just return distinct recent ones if high rated ones are scarce,
            // or just return recent ones to ensuring we have something to show.
            // For now, let's just return what we have or fall back to all recent.
            if (topReviews.isEmpty()) {
                return recent.stream().limit(3).collect(Collectors.toList());
            }
        }
        return topReviews;
    }

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }
        if (review.getCustomerName() == null || review.getCustomerName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Customer name is required");
        }
        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment is required");
        }

        review.setCreatedAt(java.time.LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        return ResponseEntity.ok(savedReview);
    }
}
