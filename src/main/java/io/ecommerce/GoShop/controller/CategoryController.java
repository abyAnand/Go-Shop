package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public String getAllCategory(Model model){


        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories",categoryList);

        return "/category/category-management";
    }

    @GetMapping("/create")
    public String addCategory(Model model){

        model.addAttribute("category",new Category());
        return "/category/add-category";
    }

    @GetMapping("/update/{id}")
    public String updateCategory(@PathVariable UUID id, RedirectAttributes attributes, Model model) {
        Optional<Category> category = categoryService.getById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "/category/update-category";
        }
        return "redirect:/category";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category,
                                 Model model,
                                 BindingResult result) {

        String name = category.getCategoryName();

        Category existingCategory = categoryService.getByCategoryName(name);

        if (existingCategory != null && !existingCategory.getId().equals(category.getId())) {
            result.rejectValue("categoryName", "error.categoryName", "Category name already exists");
            return "/category/update-category";
        }

        categoryService.save(category);

        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable UUID id,
                             RedirectAttributes attributes,
                             Model model) {

        Optional<Category> category = categoryService.getById(id);
        if (category.isPresent()) {
            categoryService.deleteById(id);
            attributes.addFlashAttribute("message", "Category deleted successfully");
            return "redirect:/category";
        }

        return "null";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,
                               Model model,
                               BindingResult result){

        String name = category.getCategoryName();

         Category theCategory = categoryService.getByCategoryName(name);

         if(theCategory!= null){
             result.rejectValue("categoryName", "error.categoryName", "Category name already exists");
             return "category/add-category";
         }

         categoryService.save(category);

        return "redirect:/category";
    }
}
