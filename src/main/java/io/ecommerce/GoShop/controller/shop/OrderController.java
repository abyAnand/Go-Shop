package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.Status;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.order.OrderService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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


        model.addAttribute("orderList", orders);

        return "/user/order-history";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getSingleOrder(@PathVariable UUID id, Model model){

        User currentUser = userService.findByUsername(getCurrentUsername()).orElse(null);

        Order order = orderService.findById(id).orElse(null);

        model.addAttribute("orderItems", order.getOrderItems());

        return "user/single-order";
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
