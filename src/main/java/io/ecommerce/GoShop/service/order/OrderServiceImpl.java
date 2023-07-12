package io.ecommerce.GoShop.service.order;


import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.Payment;
import io.ecommerce.GoShop.model.Status;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;
    @Override
    public Order createOrder(User user) {

        Order order = new Order();
        order.setUser(user);
        return order;
    }

    @Override
    public Optional<Order> getOrder(User user) {

        Order order = new Order();

//        List<Order> orderList = orderRepository.findByUser(user);
//        if(orderList.isEmpty()){
//            Order createdOrder = createOrder(user);
//            orderRepository.save(createdOrder);
//            return Optional.of(order);
//        }else{
//            return orderList;
//        }
        return null;
    }





    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getLatestOrderByUser(User user) {
        return orderRepository.getLatestOrderByUser(user);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> findByPayment(Payment payment, Pageable pageable) {
        return orderRepository.findByPayment(payment, pageable);
    }

    @Override
    public Page<Order> findByStatus(Status status, Pageable pageable) {
        return orderRepository.getAllOrders(status, pageable);
    }

    @Override
    public Page<Order> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> findByIdLike(String keyword, Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> findOrdersByDate(Date startDate, Date endDate) {
        Timestamp startTime = new Timestamp(startDate.getTime());
        Timestamp endTime = new Timestamp(endDate.getTime());
        return orderRepository.findByCreatedDateBetween(startTime, endTime);

    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

}
