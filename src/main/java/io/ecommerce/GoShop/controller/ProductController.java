package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.repository.ProductRepository;
import io.ecommerce.GoShop.service.category.CategoryService;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listProducts(Model model){

        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "product/product-management";

    }

    @GetMapping("/create")
    public String index(Model model) {

        List<Category> categoryList = categoryService.findAll();

        model.addAttribute("categories", categoryList);
        model.addAttribute("product", new Product());

        return "product/add-product";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable UUID id, Model model) {

        Optional<Product> product = productService.findById(id);
        if(product.isPresent()){
            model.addAttribute("product", product.get());
            model.addAttribute("categories", categoryService.findAll());
            return "product/update-product";
        }

            return "product/";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable UUID id, Model model, RedirectAttributes attributes) {
        productService.deleteById(id);
        attributes.addFlashAttribute("message", "Product deleted successfully");
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute Product product,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "product/update-product";
        }

        Optional<Product> existingProduct = Optional.ofNullable(productService.getByName(product.getProductName()));

        if (existingProduct.isPresent() && !existingProduct.get().getId().equals(product.getId())) {
            // Product with the same name already exists
            result.rejectValue("productName", "error.productName", "Product already exists");
            model.addAttribute("categories", categoryService.findAll());
            return "product/update-product";
        }

        productService.save(product);

        return "redirect:/products";
    }


    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              Model model,
                              BindingResult result){

        if(result.hasErrors()){
            return "product/add-product";
        }
        Product theProduct = productService.getByName(product.getProductName());

        if(theProduct != null){
            result.rejectValue("productName", "error.productName", "Product already exists");
            model.addAttribute("categories", categoryService.findAll());
            return "product/add-product";
        }

        productService.save(product);

        return "redirect:/products";
    }
}
