package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.ProductReviews;

public interface ProductReviewsRepository extends JpaRepository<ProductReviews, Integer> {
    List<ProductReviews> findByProduct_ProductID(Integer productID);
}

