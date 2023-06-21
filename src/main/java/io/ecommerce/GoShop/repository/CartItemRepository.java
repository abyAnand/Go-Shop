package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    void deleteById(UUID id);
}
