package io.ecommerce.GoShop.service.review;

import io.ecommerce.GoShop.model.Review;
import io.ecommerce.GoShop.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    ReviewRepository reviewRepository;
    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
}
