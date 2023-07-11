package io.ecommerce.GoShop.controller.app;

import io.ecommerce.GoShop.DTO.OrderDTO;
import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.Payment;
import io.ecommerce.GoShop.model.Status;
import io.ecommerce.GoShop.service.order.OrderService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderHistoryController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @GetMapping
    public String all(Model model,
                      @RequestParam(required = false) String keyword,
                      @RequestParam(required = false, defaultValue = "") String filter,
                      @PageableDefault(size = 15, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(defaultValue = "createdDate") String field,
                      @RequestParam(defaultValue = "DESC") String sort){

        pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), field));

        Page<Order> orders;


        switch (filter){
            case "ONLINE" -> orders = orderService.findByPayment(Payment.ONLINE, pageable);
            case "COD" -> orders = orderService.findByPayment( Payment.COD, pageable);
            case "PROCESSING" -> orders = orderService.findByStatus( Status.PROCESSING, pageable);
            case "SHIPPED" -> orders = orderService.findByStatus(Status.SHIPPING, pageable);
            case "DELIVERED" -> orders = orderService.findByStatus(Status.DELIVERED, pageable);
            case "CANCELLED" -> orders = orderService.findByStatus(Status.CANCELLED, pageable);
            case "RETURNED" -> orders = orderService.findByStatus(Status.RETURNED, pageable);
            case "REFUNDED" -> orders = orderService.findByStatus(Status.REFUNDED, pageable);
            case "CONFIRMED" -> orders = orderService.findByStatus(Status.CONFIRMED, pageable);
            default -> {
                if (keyword == null || keyword.equals("")) {
                    orders = orderService.findAllOrders(pageable);
                } else {
                    orders = orderService.findByIdLike(keyword, pageable);
                }
            }
        }





        model.addAttribute("orders", orders);


        //Pagination Values
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("field", field);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", size);
        int startPage = Math.max(0, page - 1);
        int endPage = Math.min(page + 1, orders.getTotalPages() - 1);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("empty", orders.getTotalElements() == 0);


        return "app-admin/order/order-management";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getSingleOrder(@PathVariable UUID id, Model model){

        Optional<Order> order = orderService.findById(id);

        List<Status> statusList = Arrays.asList(Status.values());

        model.addAttribute("statusList", statusList);
        model.addAttribute("order", order.get());

//        model.addAttribute("orderItems", order.getOrderItems());

        return "app-admin/order/edit-order";
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



}
