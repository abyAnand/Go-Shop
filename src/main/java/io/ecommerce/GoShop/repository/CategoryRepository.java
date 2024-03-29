package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category getByCategoryName(String name);

    Page<Category> findByCategoryName(Pageable pageable, String keyword);
}
