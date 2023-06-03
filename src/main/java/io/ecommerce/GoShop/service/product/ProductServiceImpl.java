package io.ecommerce.GoShop.service.product;

import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;


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

}
