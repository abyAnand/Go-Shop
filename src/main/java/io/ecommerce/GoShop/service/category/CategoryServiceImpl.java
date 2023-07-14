package io.ecommerce.GoShop.service.category;

import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public Category getByCategoryName(String name) {
        return categoryRepository.getByCategoryName(name);
    }

    @Override
    public void save(Category category) {
         categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getById(UUID id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        categoryRepository.findById(id)
                .ifPresent(category -> {
                    category.setDeleted(true);
                    categoryRepository.save(category);
                });

    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Page<Category> findByCategoryName(Pageable pageable, String keyword) {
        return categoryRepository.findByCategoryName(pageable, keyword);
    }
}
