package io.ecommerce.GoShop.store.controller;

import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.category.CategoryService;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getAllProducts(Model model,
                                 @RequestParam(name = "field", required = false, defaultValue = "productName") String field,
                                 @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
                                 @RequestParam(name ="page",required = false, defaultValue = "0") int page,
                                 @RequestParam(name ="size",required = false, defaultValue = "5") int size,
                                 @RequestParam(name ="keyword",required = false) String keyword,
                                 @RequestParam(name ="filter",required = false, defaultValue = "") String filter){

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sort),field));

        Page<Product> products = Page.empty();

        if(keyword == null || keyword.equals("")){
            products = productService.findAll(pageable);
        }else{
            products = productService.findByName(pageable, keyword);
        }

        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("field", field);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", size);
        int startPage = Math.max(0, page - 1);
        int endPage = Math.min(page + 1, products.getTotalPages() - 1);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("empty", products.getTotalElements() == 0);

        return "category";
    }


    @GetMapping("product")
    public String getProductByName(Model model,
                                   @RequestParam(name = "field", required = false, defaultValue = "productName") String field,
                                   @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
                                   @RequestParam(name ="page",required = false, defaultValue = "0") int page,
                                   @RequestParam(name ="size",required = false, defaultValue = "5") int size,
                                   @RequestParam(name ="keyword",required = false) String keyword,
                                   @RequestParam(name ="filter",required = false, defaultValue = "") String filter){

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sort),field));


        Page<Product> products = productService.getByNamePaged(keyword, pageable);


        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("field", field);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", size);
        int startPage = Math.max(0, page - 1);
        int endPage = Math.min(page + 1, products.getTotalPages() - 1);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("empty", products.getTotalElements() == 0);

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
