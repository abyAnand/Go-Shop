package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> getCartByUser(User user);

}
