package com.example.demo.Service;

import java.util.List;

import com.example.demo.Entity.Category;
import com.example.demo.Request.CategoryCount;

public interface CategoryService {
    List<Category> findAll();
    Category findById(String categoryID);
    public List<CategoryCount> findAllCategoriesWithProductCount();
    Boolean create(Category category);
    Boolean update(Category category);
    Boolean delete(String categoryID);


}
