package io.ecommerce.GoShop.service.order;


import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}