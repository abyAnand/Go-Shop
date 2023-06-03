package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.Image;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.repository.ProductRepository;
import io.ecommerce.GoShop.service.category.CategoryService;
import io.ecommerce.GoShop.service.image.ImageService;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

    @Autowired
    private ImageService imageService;

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
    public String saveProduct(@ModelAttribute Product product, BindingResult result,
                              @RequestParam("images") List<MultipartFile> imageFiles,
                              Model model) throws IOException {

        product = productService.save(product);


        List<Image> images = new ArrayList<>();
        if(!imageFiles.get(0).getOriginalFilename().equals("")){
            for (MultipartFile image : imageFiles) {
                String fileLocation = handleFileUpload(image); // Save the image and get its file location
                Image imageEntity = new Image(fileLocation,product); // Create an Image entity with the file location
                imageEntity = imageService.saveImage(imageEntity);
                images.add(imageEntity); // Add the Image entity to the Product's list of images
            }
        }

        return "/index";
    }

    private String handleFileUpload(MultipartFile file) throws IOException {
        // Define the directory to save the file in
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/images/product";


        // Create the directory if it doesn't exist
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate a unique file name for the uploaded file
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Save the file to the upload directory
        String filePath = uploadDir + "/" + fileName;
        Path path = Paths.get(filePath);
        System.out.println(path);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Return the file path
        return fileName;
    }
    private void handleDelete(String fileName) throws IOException {
        // Define the directory
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/images/product";

        // Get the file path
        String filePath = uploadDir + "/" + fileName;

        // Create a file object for the file to be deleted
        File file = new File(filePath);

        // Check if the file exists
        if (file.exists()) {
            // Delete the file
            file.delete();
            System.out.println("File deleted successfully!");
        } else {
            System.out.println("File not found!");
        }
    }
}
