package io.ecommerce.GoShop.service.product;

import io.ecommerce.GoShop.model.Image;
import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.repository.ImageRepository;
import io.ecommerce.GoShop.repository.ProductRepository;
import io.ecommerce.GoShop.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;


    @Override
    public Product getByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public Product save(Product product) {
         return productRepository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(UUID id) {
         return productRepository.findById(id);
    }

    @Override
    public boolean existsByName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    @Override
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> findByName(String productName) {
        return Optional.ofNullable(productRepository.findByProductName(productName));
    }


    @Override
    public void deleteImage(Image deletedImage) {
        // Delete the image from the image database
        imageService.deleteImage(deletedImage);

        // Remove the image from the product
        Product product = deletedImage.getProduct();
        product.getImages().remove(deletedImage);
        save(product);
    }

    public void deleteImageById(UUID imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        if (image.isPresent()) {
            Optional<Product> product = productRepository.findById(image.get().getProduct().getId());
            if (product.isPresent()) {
                product.get().getImages().removeIf(img -> img.getId().equals(imageId));
                productRepository.save(product.get());
            }
            imageRepository.deleteById(imageId);
        }
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }


}
