package io.ecommerce.GoShop.service.product;

import io.ecommerce.GoShop.model.Image;
import io.ecommerce.GoShop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProductService {



    Product getByName(String productName);

    Product save(Product product);


    List<Product> getAll();

    Optional<Product> findById(UUID id);

    boolean existsByName(String productName);


    void deleteById(UUID id);

    Optional<Product> findByName(String productName);

    void deleteImage(Image deletedImage);

    void deleteImageById(UUID imageId);

    List<Product> findAll();

    Optional<Product> getProductById(UUID id);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByName(Pageable pageable, String keyword);

    Page<Product> getByNamePaged(String keyword, Pageable pageable);
}
