package io.ecommerce.GoShop.service.category;

import io.ecommerce.GoShop.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {


    Category getByCategoryName(String name);

    void save(Category category);

    List<Category> findAll();

    Optional<Category> getById(UUID id);

    void deleteById(UUID id);
}
