package io.ecommerce.GoShop.service.variant;

import io.ecommerce.GoShop.model.Variant;
import io.ecommerce.GoShop.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VariantServiceImpl implements VariantService{

    @Autowired
    VariantRepository variantRepository;
    @Override
    public List<Variant> getAllVariants() {
        return variantRepository.findAll();
    }

    @Override
    public List<Variant> getAllVariantsByProductId(UUID productId) {
        return variantRepository.getAllVariantsByProductId(productId);
    }

    @Override
    public Optional<Variant> findByName(String variantName) {
        return variantRepository.findByVariantName(variantName);
    }

    @Override
    public Variant save(Variant variant) {
        return variantRepository.save(variant);
    }

    @Override
    public void deleteById(UUID id) {
         variantRepository.deleteById(id);
    }

    @Override
    public Optional<Variant> findByProductIdAndVariantName(UUID productId, String variantName) {
        return variantRepository.findByProductIdAndVariantName(productId, variantName);
    }

    @Override
    public Optional<Variant> findById(UUID id) {
        return variantRepository.findById(id);
    }

}
