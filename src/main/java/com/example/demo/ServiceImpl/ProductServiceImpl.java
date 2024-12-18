package com.example.demo.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Service.ProductService;

@Service

public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository dao;

    @Override
    public List<Product> findAll() {
        List<Product> products = dao.findAll();
        System.out.println("Số lượng sản phẩm lấy được: " + products.size());
        return dao.findAll();
    }

    @Override
    public List<String> findImagesByProductId(int productID) {
        // TODO Auto-generated method 
        return dao.findImagesByProductId(productID); // Gọi phương thức truy vấn

    }

    @Override
    public List<Product> findByCategoryId(String categoryID) {
        // TODO Auto-generated method stub
       return dao.findByCategoryId(categoryID);
    }

    @Override
    public Product finById(int productID) {
        return dao.findById(productID).orElse(null); // Xử lý Optional để tránh lỗi
    }

    @Override
    public long getTotalProductsByCategory(String categoryID) {
        return dao.countByCategory(categoryID);
    }


}