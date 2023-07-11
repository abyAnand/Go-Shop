package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.model.Variant;
import io.ecommerce.GoShop.service.product.ProductService;
import io.ecommerce.GoShop.service.variant.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value={"variants", "variant"})
public class VariantController {

    @Autowired
    ProductService productService;

    @Autowired
    VariantService variantService;


    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(name = "field", required = false, defaultValue = "productName") String field,
                               @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
                               @RequestParam(name ="page",required = false, defaultValue = "0") int page,
                               @RequestParam(name ="size",required = false, defaultValue = "10") int size,
                               @RequestParam(name ="keyword",required = false) String keyword,
                               @RequestParam(name ="filter",required = false, defaultValue = "") String filter){

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sort),field));

        Page<Product> products = Page.empty();

        if(keyword == null || keyword.equals("")){
            products = productService.findAll(pageable);
        }else{
            products = productService.findByName(pageable, keyword);
        }
        // Replace this with the logic to fetch the products
        model.addAttribute("products", products);

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

        return "app-admin/variant/variant-management-product";
    }


    @GetMapping("/{productId}")
    public String getAll(@PathVariable("productId")UUID id, Model model){

        List<Variant> variantList = variantService.getAllVariantsByProductId(id);

        Optional<Product> product = productService.findById(id);
        String productName = product.get().getProductName();

        model.addAttribute("productName", productName);
        model.addAttribute("productId", id);
        model.addAttribute("variants", variantList);


        return "app-admin/variant/variant-management";
    }


    @GetMapping("/create/{productId}")
    public String createVariant(@PathVariable("productId") UUID productId, Model model){

        Optional<Product> product = productService.findById(productId);
        String productName = product.get().getProductName();

        model.addAttribute("productName", productName);
        model.addAttribute("productId", productId);
        model.addAttribute("variant", new Variant());


        return "app-admin/variant/create-variant";
    }

    @PostMapping("/save")
    public String saveVariant(@ModelAttribute Variant variant,
                              @RequestParam(value = "productId") UUID productId,
                              Model model,
                              BindingResult result){

        Optional<Product> product = productService.findById(productId);
        String productName = product.get().getProductName();

        Optional<Variant> existingVariant = variantService.findByNameAndProduct(variant.getVariantName(), product.get());

        if (existingVariant.isPresent()) {
            result.rejectValue("variantName", "error.variantName", "Variant already exists");


            model.addAttribute("productName", productName);
            model.addAttribute("variant", variant);
            model.addAttribute("productId", productId);
            return "variant/create-variant";
        }

        variant.setProduct(product.get());
        variant = variantService.save(variant);


        return "redirect:/variants/" + productId;
    }

    @GetMapping("/delete/{id}/{variantId}")
    public String deleteVariant(@PathVariable("id") UUID id ,
                                @PathVariable("variantId") UUID variantId ,
                                RedirectAttributes attributes){
        variantService.deleteById(variantId);
        attributes.addFlashAttribute("message", "Product deleted successfully");
        return "redirect:/variants/" + id;
    }


    @GetMapping("/edit/{id}")
    public String editVarient(@PathVariable UUID id, Model model){



        Optional<Variant> variant = variantService.findById(id);
        UUID varId = variant.get().getId();
        UUID productId = variant.get().getProduct().getId();
        String productName = variant.get().getProduct().getProductName();

        model.addAttribute("productName", productName);
        model.addAttribute("variant", variant.get());
        model.addAttribute("variantId", varId);
        model.addAttribute("productId", productId);
        return "app-admin/variant/edit-variant";
    }

    @PostMapping("/update")
    public String updateVariant(@ModelAttribute("variant") Variant variant,
                                @RequestParam(value = "productId") UUID productId,
                                BindingResult result,
                                Model model) {

        // Retrieve the product ID and variant ID
        UUID variantId = variant.getId();

        // Check if the variant name already exists for the given product
        Optional<Variant> existingVariant = variantService.findByProductIdAndVariantName(productId, variant.getVariantName());

        if (existingVariant.isPresent() && !existingVariant.get().getId().equals(variantId)) {
            result.rejectValue("variantName", "error.variantName", "Variant name already exists for the product");
            model.addAttribute("productName", productService.findById(productId).get().getProductName());
            return "variant/update-variant"; // Update the view name accordingly
        }

        Optional<Product> product = productService.findById(productId);
        variant.setProduct(product.get());

        // Update the variant
        variantService.save(variant);

        return "redirect:/variants/" + productId; // Redirect to the appropriate URL
    }
}
