package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {

    List<Variant> getAllVariantsByProductId(UUID productId);

    Optional<Variant> findByVariantName(String variantName);

    Optional<Variant> findByProductIdAndVariantName(UUID productId, String variantName);
}
