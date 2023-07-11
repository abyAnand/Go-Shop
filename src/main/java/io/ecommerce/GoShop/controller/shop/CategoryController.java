package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/category")

public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllCategory(Model model,
                                 @RequestParam(name = "field", required = false, defaultValue = "categoryName") String field,
                                 @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
                                 @RequestParam(name ="page",required = false, defaultValue = "0") int page,
                                 @RequestParam(name ="size",required = false, defaultValue = "5") int size,
                                 @RequestParam(name ="keyword",required = false) String keyword,
                                 @RequestParam(name ="filter",required = false, defaultValue = "") String filter){

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sort),field));

        Page<Category> categoryList = Page.empty();

        if(keyword == null || keyword.equals("")){
            categoryList = categoryService.findAll(pageable);
        }else{
            categoryList = categoryService.findByCategoryName(pageable, keyword);
        }

        //        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryList.getTotalPages());
        model.addAttribute("field", field);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", size);
        int startPage = Math.max(0, page - 1);
        int endPage = Math.min(page + 1, categoryList.getTotalPages() - 1);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("empty", categoryList.getTotalElements() == 0);
        model.addAttribute("categories",categoryList);

        return "app-admin/category/category-management";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/create")
    public String addCategory(Model model){

        model.addAttribute("category",new Category());
        return "app-admin/category/create-category";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String updateCategory(@PathVariable UUID id, RedirectAttributes attributes, Model model) {
        Optional<Category> category = categoryService.getById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "app-admin/category/edit-category";
        }
        return "redirect:/category";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category,
                                 Model model,
                                 BindingResult result) {

        String name = category.getCategoryName();

        Category existingCategory = categoryService.getByCategoryName(name);

        if (existingCategory != null && !existingCategory.getId().equals(category.getId())) {
            result.rejectValue("categoryName", "error.categoryName", "Category name already exists");
            return "app-admin/category/update-category";
        }

        categoryService.save(category);

        return "redirect:/category";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

        return "redirect:/category";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,
                               Model model,
                               BindingResult result){

        String name = category.getCategoryName();

         Category theCategory = categoryService.getByCategoryName(name);

         if(theCategory!= null){
             result.rejectValue("categoryName", "error.categoryName", "Category name already exists");
             return "app-admin/category/add-category";
         }

         categoryService.save(category);

        return "redirect:/category";
    }
}
