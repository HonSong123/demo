package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, String>{
    List<ProductImage> findByProduct_ProductID(int productID);
}