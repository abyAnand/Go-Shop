package io.ecommerce.GoShop.store.controller;

import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.service.category.CategoryService;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/store")
public class StoreController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = {"/","/index"})
    public String homePage(){
        return "index";
    }

    @GetMapping("/products")
    public String getAllProducts(Model model){
        List<Category> categories = categoryService.findAll();
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "category";
    }

    @GetMapping("/category/{id}")
    public String getByCategory(@PathVariable UUID id, Model model){
        Optional<Category> category = categoryService.getById(id);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", category.get().getProduct());
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory",category.get().getId());
        return "category";
    }

    @GetMapping("/products/{id}")
    public String getSingleProduct(@PathVariable UUID id, Model model){

        Optional<Product> product = productService.getProductById(id);
        product.ifPresent(value -> model.addAttribute("product", value));

        return "single-product";
    }



}
