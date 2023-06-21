package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser(User user);

    Order getLatestOrderByUser(User user);

    Optional<Cart> getOrderByUser(User user);


}
