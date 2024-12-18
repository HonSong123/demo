package com.example.demo.Service;

import java.util.List;

import com.example.demo.Entity.Product;

public interface ProductService {
    public List<Product> findAll() ;
    List<String> findImagesByProductId(int productID); // Thêm phương thức này
    public List<Product> findByCategoryId(String categoryID) ;
	Product finById(int productID);
    public long getTotalProductsByCategory(String categoryID);
} 
