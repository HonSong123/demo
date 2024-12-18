package com.example.demo.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.ProductReviews;
import com.example.demo.Repository.ProductReviewsRepository;

@Service
public class ProductReviewService {
     @Autowired
    private ProductReviewsRepository repository;

    public ProductReviews saveReview(ProductReviews review) {
        review.setReviewDate(new Date()); // Ghi lại ngày đánh giá
        return repository.save(review);
    }

    public List<ProductReviews> getReviewsByProduct(Integer productId) {
        return repository.findByProduct_ProductID(productId);
    }
}
