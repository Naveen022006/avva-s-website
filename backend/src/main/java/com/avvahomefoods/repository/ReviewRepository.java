package com.avvahomefoods.repository;

import com.avvahomefoods.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findTop3ByRatingOrderByCreatedAtDesc(int rating);

    List<Review> findAllByOrderByCreatedAtDesc();
}
