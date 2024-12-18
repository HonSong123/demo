package com.example.demo.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Request.CategoryCount;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/category")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;
    @GetMapping("/all")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    // Endpoint to get products by category ID
    @GetMapping("/{categoryID}/products")
    public List<Product> getProductsByCategory(@PathVariable String categoryID) {
        return productService.findByCategoryId(categoryID);
    }

    @GetMapping
    public List<CategoryCount> getAllCategoriesWithProductCount() {
        return categoryService.findAllCategoriesWithProductCount();
    }
    
}
