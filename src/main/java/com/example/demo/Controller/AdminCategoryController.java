package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Entity.Category;
import com.example.demo.Service.CategoryService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;











@Controller
@RequestMapping("/admin")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/category")
    public String index (Model model) {
        List<Category> list = this.categoryService.findAll();
        model.addAttribute("listCategory", list);
        return "admin/category/index";
    }

    @GetMapping("/add-category")
    public String add (Model model) {
        Category category = new Category();
        model.addAttribute("category",category);
        return "admin/category/add";
    }

    @PostMapping("/add-category")
    public String save(@ModelAttribute("category") Category category) {
        //TODO: process POST request
        if(this.categoryService.create(category)){
        return "redirect:/admin/category";
        } else{
            return "admin/category/add";
        }
    }

    @GetMapping("/edit-category/{categoryID}")
    public String edit(Model model, @PathVariable("categoryID") String categoryID) {

        Category category = this.categoryService.findById(categoryID);
        model.addAttribute("category",category);
        return "admin/category/edit";
    }
    @PostMapping("/edit-category")
    public String edit(@ModelAttribute("category") Category category) {
        //TODO: process POST request
        if(this.categoryService.create(category)){
        return "redirect:/admin/category";
        } else{
            return "admin/category/add";
        }
    }


    @GetMapping("/delete-category/{categoryID}")
    public String delete(@PathVariable("categoryID") String categoryID) {
        //TODO: process POST request
        if(this.categoryService.delete(categoryID)){
        return "redirect:/admin/category";
        } else{
            return "redirect:/admin/category";
        }
    }

}
