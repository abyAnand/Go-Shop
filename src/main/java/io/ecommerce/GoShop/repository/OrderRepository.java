package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser(User user);

    Order getLatestOrderByUser(User user);

    Optional<Cart> getOrderByUser(User user);


    Page<Order> findByPayment(Payment payment, Pageable pageable);




    @Query("SELECT o FROM orders o WHERE o.status = :status")
    Page<Order> getAllOrders(Status status, Pageable pageable);

    List<Order> findByCreatedDateBetween(Date startDate, Date endDate);
}
