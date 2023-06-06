package io.ecommerce.GoShop.store.controller;

import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users/products")
public class StoreController {

    @Autowired
    ProductService productService;

    @GetMapping
    public String getAllProducts(Model model){

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        return "category";
    }
}
