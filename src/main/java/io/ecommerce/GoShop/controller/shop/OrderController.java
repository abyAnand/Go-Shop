package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.DTO.OrderDTO;
import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.Status;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.order.OrderService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderController {


    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;


//    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }



    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getOrders(Model model){

        User currentUser = userService.findByUsername(getCurrentUsername()).orElse(null);

        List<Order> orders = currentUser.getOrders();
        Collections.sort(orders, Comparator.comparing(Order::getCreatedDate).reversed());

        model.addAttribute("user",currentUser);


        model.addAttribute("orderList", orders);

        return "user/order-dashboard";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getSingleOrder(@PathVariable UUID id, Model model){

        User currentUser = userService.findByUsername(getCurrentUsername()).orElse(null);

        Order order = orderService.findById(id).orElse(null);

        model.addAttribute("orderItems", order.getOrderItems());

//        model.addAttribute("orderItems", order.getOrderItems());

        return "user/single-order-user";
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody OrderDTO order) {

        System.out.println(order.getOrderId());

        orderService.findById(order.getOrderId())
                .ifPresent(existingOrder -> {
                    existingOrder.setStatus(order.getStatus());
                    orderService.saveOrder(existingOrder);
                });


        String response = "Order updated successfully";
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String cancelOrder(@PathVariable UUID id, Model model){

        User currentUser = userService.findByUsername(getCurrentUsername()).orElse(null);

        Order order = orderService.findById(id).orElse(null);
        order.setStatus(Status.CANCELLED);
        orderService.saveOrder(order);
        model.addAttribute("orderItems", order.getOrderItems());

        return "redirect:/orders";
    }



}
