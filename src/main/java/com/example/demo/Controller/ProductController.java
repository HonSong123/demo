package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Service.CategoryService;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/product")
    public String index() {
        return "admin/product/index";
    }

    @RequestMapping("/product-add")
    public String add(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);

        List<Category> listCate = this.categoryService.findAll();
        model.addAttribute("listCate", listCate);

        return "admin/product/add";
    }

    // @PostMapping("/product-add")
    // public String save(@ModelAttribute("product") Product product) {
    // // TODO: process POST request

    // return entity;
    // }

}
