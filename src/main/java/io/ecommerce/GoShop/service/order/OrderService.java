package io.ecommerce.GoShop.service.order;

import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    Order createOrder(User user);

    Optional<Order> getOrder(User user);


    Order saveOrder(Order order);

    Order getLatestOrderByUser(User user);

    Optional<Order> findById(UUID orderId);

    List<Order> getAllOrders();
}
