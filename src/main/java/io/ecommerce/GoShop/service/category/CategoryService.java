package io.ecommerce.GoShop.service.category;

import io.ecommerce.GoShop.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {


    Category getByCategoryName(String name);

    void save(Category category);

    List<Category> findAll();

    Optional<Category> getById(UUID id);

    void deleteById(UUID id);

    Page<Category> findAll(Pageable pageable);

    Page<Category> findByCategoryName(Pageable pageable, String keyword);
}
