package io.ecommerce.GoShop.controller.shop;


import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.Coupon;
import io.ecommerce.GoShop.model.CouponType;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.service.category.CategoryService;
import io.ecommerce.GoShop.service.coupon.CouponService;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CouponService couponService;


    @GetMapping
    public String getAllCoupons(Model model){

        List<Coupon> couponList = couponService.findAll();
        model.addAttribute("couponList", couponList);

        return "coupon/coupon-management";
    }

    @GetMapping("/create")
    public String createCoupon(Model model){
        List<Product> productList = productService.findAll();
        List<Category> categoryList = categoryService.findAll();


        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        model.addAttribute("coupon", new Coupon());
        return "coupon/create-coupon";
    }


    @PostMapping("/save")
    public String saveCoupon(@ModelAttribute Coupon coupon,
                             BindingResult result, Model model) {

        if (couponService.findByCode(coupon.getCode()).isPresent()) {
            result.rejectValue("code", "error.coupon", "Coupon code must be unique");
            model.addAttribute("categoryList", productService.findAll());
            model.addAttribute("productList", categoryService.findAll());
            model.addAttribute("coupon", new Coupon());
            return "coupon/create-coupon";
        }else{
            if (coupon.getType() == CouponType.PRODUCT) {
                coupon.setCategory(null);
            }else if (coupon.getType() == CouponType.CATEGORY){
                coupon.setProduct(null);
            }else {
                coupon.setProduct(null);
                coupon.setCategory(null);
            }
            couponService.save(coupon);
            return "redirect:/coupon";
        }

    }

    @GetMapping("/edit/{id}")
    public String editCoupon(@PathVariable String id, Model model){

        UUID uuid = UUID.fromString(id);
        Coupon coupon = couponService.findById(uuid).orElse(null);
        model.addAttribute("coupon", coupon);
        return "coupon/update-coupon";
    }

    @GetMapping("/delete/{id}")
    public String deleteCoupon(@PathVariable String id, Model model){

        UUID uuid = UUID.fromString(id);
        Coupon coupon = couponService.findById(uuid).orElse(null);
        couponService.deleteCoupon(coupon);
        return "redirect:/coupon/";
    }

    @PostMapping("/update")
    public String updateCoupon(@ModelAttribute Coupon coupon){

        return "redirect:/coupon/";
    }
}
