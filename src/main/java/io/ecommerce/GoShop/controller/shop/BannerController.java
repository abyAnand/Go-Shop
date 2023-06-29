package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.model.Banner;
import io.ecommerce.GoShop.model.BannerImage;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.service.banner.BannerService;
import io.ecommerce.GoShop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private ProductService productService;


    @GetMapping
    public String getAllBanner(Model model){


        model.addAttribute("bannerList", bannerService.findAll());
        return "banner/banner-management";
    }

    @GetMapping("/create")
    public String createBanner(Model model) {

        model.addAttribute("productList", productService.findAll());
        model.addAttribute("banner", new Banner());
        return "banner/create-banner";
    }

    @PostMapping("/save")
    public String saveBanner(@ModelAttribute("banner") Banner banner,
                             BindingResult result,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            return "banner/create-banner";
        }

        String fileName = handleFileUpload(imageFile);
        BannerImage image = new BannerImage(fileName, banner);
        banner.setImage(image);

        bannerService.save(banner);

        return "redirect:/banner";
    }

    @GetMapping("/edit/{id}")
    public String editBanner(@PathVariable("id") UUID id, Model model) {

        Optional<Banner> banner = bannerService.findById(id);
        model.addAttribute("banner", banner.get());

        return "banner/update-banner";
    }

    @PostMapping("/update")
    public String updateBanner(@ModelAttribute("banner") Banner banner,
                               BindingResult result,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            return "banner/edit-banner";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = handleFileUpload(imageFile);
            BannerImage image = new BannerImage(fileName, banner);
            banner.setImage(image);
        }

        bannerService.save(banner);

        return "redirect:/banner";
    }

    @GetMapping("/delete/{id}")
    public String deleteBanner(@PathVariable("id") Long id) {

        UUID uuid = UUID.fromString(String.valueOf(id));
        Optional<Banner> banner = bannerService.findById(uuid);
        bannerService.deletebyId(uuid);
        return "redirect:/banner";
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
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        System.gc();

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