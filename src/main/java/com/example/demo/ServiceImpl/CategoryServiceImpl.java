package com.example.demo.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Request.CategoryCount;
import com.example.demo.Service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository daoLH;

   

    @Override
    public List<Category> findAll() {
        // TODO Auto-generated method stub
        return daoLH.findAll();
    }
    @Override
    public Category findById(String categoryID) {
        return daoLH.findById(categoryID).orElse(null);
    }
    @Override
    public List<CategoryCount> findAllCategoriesWithProductCount() {
         return daoLH.findAll().stream()
            .map(category -> new CategoryCount(
                category.getCategoryID(),
                category.getCategoryname(),
                category.getProduct().size())) // Get product count here
            .collect(Collectors.toList());
    }

    @Override
    public Boolean create(Category category) {
        try {
            this.daoLH.save(category);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;

    }
    @Override
    public Boolean update(Category category) {
        try {
            this.daoLH.save(category);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;

    }

    @Override
    public Boolean delete(String categoryID) {
        try {
            this.daoLH.delete(findById(categoryID));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}