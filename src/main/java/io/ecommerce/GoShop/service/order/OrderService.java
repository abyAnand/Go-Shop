package io.ecommerce.GoShop.service.order;

import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.Payment;
import io.ecommerce.GoShop.model.Status;
import io.ecommerce.GoShop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<Order> findByPayment(Payment payment, Pageable pageable);

    Page<Order> findByStatus(Status status, Pageable pageable);

    Page<Order> findAllOrders(Pageable pageable);

    Page<Order> findByIdLike(String keyword, Pageable pageable);
}
