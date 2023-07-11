package io.ecommerce.GoShop.controller.shop;


import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.Coupon;
import io.ecommerce.GoShop.model.CouponType;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.repository.CouponRepository;
import io.ecommerce.GoShop.service.category.CategoryService;
import io.ecommerce.GoShop.service.coupon.CouponService;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private CouponRepository couponRepository;


    @GetMapping
    public String getAllCoupons(Model model,
                                @RequestParam(name = "field", required = false, defaultValue = "type") String field,
                                @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
                                @RequestParam(name ="page",required = false, defaultValue = "0") int page,
                                @RequestParam(name ="size",required = false, defaultValue = "10") int size,
                                @RequestParam(name ="keyword",required = false) String keyword,
                                @RequestParam(name ="filter",required = false, defaultValue = "") String filter) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sort),field));
        Page<Coupon> coupons = Page.empty();
        if(keyword == null || keyword.equals("")){
            coupons = couponService.findAllPaged(pageable);
        }else{
            coupons = couponService.findByCodeLike(pageable, keyword);
        }

            model.addAttribute("filter", filter);
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", coupons.getTotalPages());
            model.addAttribute("sort", sort);
            model.addAttribute("pageSize", size);
            model.addAttribute("field", field);
            int startPage = Math.max(0, page - 1);
            int endPage = Math.min(page + 1, coupons.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("empty", coupons.getTotalElements() == 0);


            model.addAttribute("coupons", coupons);

            coupons.stream()
                    .map(Coupon::isExpired)
                    .forEach(System.out::println);

            return "app-admin/coupon/coupon-management";
        }


    @GetMapping("/create")
    public String createCoupon(Model model){
        List<Product> productList = productService.findAll();
        List<Category> categoryList = categoryService.findAll();


        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        model.addAttribute("coupon", new Coupon());
        return "app-admin/coupon/create-coupon";
    }


    @PostMapping("/save")
    public String saveCoupon(@ModelAttribute Coupon coupon,
                             BindingResult result, Model model) {

        if (couponService.findByCode(coupon.getCode()).isPresent() && !couponService.findByCode(coupon.getCode()).get().isDeleted()) {
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

        List<Product> productList = productService.findAll();
        List<Category> categoryList = categoryService.findAll();

        UUID uuid = UUID.fromString(id);
        Coupon coupon = couponService.findById(uuid).orElse(null);
        model.addAttribute("coupon", coupon);
        return "app-admin/coupon/edit-coupon";
    }

    @GetMapping("/delete/{id}")
    public String deleteCoupon(@PathVariable String id, Model model){

        UUID uuid = UUID.fromString(id);
        Coupon coupon = couponService.findById(uuid).orElse(null);
        coupon.setDeleted(true);
        couponService.save(coupon);
        return "redirect:/coupon/";
    }

    @PostMapping("/update")
    public String updateCoupon(@ModelAttribute Coupon coupon){

        //TODO: NEW TEMPLATE CHECK THE UPDATE SPECIFICALLY THE DATE

        Coupon existingCoupon = couponService.findById(coupon.getId()).orElse(null);


        existingCoupon.setCouponStock(coupon.getCouponStock());
        existingCoupon.setExpirationPeriod(coupon.getExpirationPeriod());
        existingCoupon.setDiscount(coupon.getDiscount());
        existingCoupon.setMaximumDiscountAmount(coupon.getMaximumDiscountAmount());

        couponService.save(existingCoupon);

        System.out.println(coupon);

//        couponService.save(coupon);

        return "redirect:/coupon/";
    }
}
