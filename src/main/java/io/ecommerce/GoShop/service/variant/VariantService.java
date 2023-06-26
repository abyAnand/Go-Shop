package io.ecommerce.GoShop.service.variant;

import io.ecommerce.GoShop.model.Product;
import io.ecommerce.GoShop.model.Variant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VariantService {

    public List<Variant> getAllVariants();

    public List<Variant> getAllVariantsByProductId(UUID productId);

    Optional<Variant> findByName(String variantName);

    Variant save(Variant variant);

    void deleteById(UUID id);

    Optional<Variant> findByProductIdAndVariantName(UUID productId, String variantName);

    Optional<Variant> findById(UUID id);

    Optional<Variant> findByNameAndProduct(String variantName, Product product);
}
