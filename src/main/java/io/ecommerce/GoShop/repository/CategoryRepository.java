package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category getByCategoryName(String name);
}
