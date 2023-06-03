package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Image;
import io.ecommerce.GoShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByProductName(String name);


    boolean existsByProductName(String productName);


}
