package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.model.*;
import io.ecommerce.GoShop.service.order.OrderService;
import io.ecommerce.GoShop.service.order.OrderServiceImpl;
import io.ecommerce.GoShop.service.role.RoleService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    public String userList(Model model,
                           @RequestParam(name = "field", required = false, defaultValue = "username") String field,
                           @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
                           @RequestParam(name ="page",required = false, defaultValue = "0") int page,
                           @RequestParam(name ="size",required = false, defaultValue = "5") int size,
                           @RequestParam(name ="keyword",required = false) String keyword,
                           @RequestParam(name ="filter",required = false, defaultValue = "") String filter) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sort),field));

        Page<User> userList = Page.empty();

        if(keyword == null || keyword.equals("")){
             userList = userService.findAll(pageable);
        }else{
             userList = userService.findByUsername(pageable, keyword);
        }

        model.addAttribute("users", userList);

//        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userList.getTotalPages());
        model.addAttribute("field", field);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", size);
        int startPage = Math.max(0, page - 1);
        int endPage = Math.min(page + 1, userList.getTotalPages() - 1);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("empty", userList.getTotalElements() == 0);

        return "/admin/user-management";
    }

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@GetMapping("/users/create")
public String createUser(Model model){

    List<Role> roles = roleService.getRoles();
    model.addAttribute("user", new User());
    model.addAttribute("roles", roles);


        return "/admin/create-user";
}

    @GetMapping("/users/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateUser(@PathVariable UUID id, Model model){

        User user = userService.findById(id);
        List<Role> role = roleService.getRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", role);


        return "/admin/update-user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable UUID id,
                             RedirectAttributes attributes,
                             Model model) {

        Optional<User> user = Optional.ofNullable(userService.findById(id));

        if (user.isPresent()) {
            UUID userId = user.get().getId();
            userService.delete(userId);
            attributes.addFlashAttribute("message", "User deleted successfully");
        }

        return "redirect:/admin/users";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                           BindingResult result,
                           Model model) {

        List<Role> roles = roleService.getRoles();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            return "/admin/update-user";
        }

        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            result.rejectValue("username", "error.username", "Username already exists");
            model.addAttribute("roles", roles);
            return "/admin/update-user";
        }

        Wallet wallet = user.getWallet();
        double newBalance = wallet != null ? wallet.getBalance() : 0.0;

        if (existingUser.isPresent() && existingUser.get().getWallet() != null) {
            Wallet existingWallet = existingUser.get().getWallet();
            double oldBalance = existingWallet.getBalance();
            double updatedBalance = oldBalance + newBalance;
            existingWallet.setBalance(updatedBalance);

            Transaction transaction = newBalance > oldBalance ? Transaction.CREDITED :
                    newBalance < oldBalance ? Transaction.DEBITED : null;

            double amount = Math.abs(newBalance - oldBalance);

            if (transaction != null) {
                WalletHistory walletHistory = WalletHistory.builder()
                        .wallet(existingWallet)
                        .amount(String.valueOf(amount))
                        .transaction(transaction)
                        .build();
                if (existingWallet.getHistory() != null) {
                    existingWallet.getHistory().add(walletHistory);
                }
            }

            existingWallet.setBalance(newBalance);
            user.setWallet(existingWallet);
        } else {
            if (wallet != null) {
                wallet.setUser(user);
                user.setWallet(wallet);
            }
        }

// Save the user
        if (user.getPassword().equals("")) {
            user.setPassword(existingUser.get().getPassword());
             userService.save(user);
        } else {
            userService.saveUserWithEncodedPassword(user);
        }


        return "redirect:/admin/users";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/saveNewUser")
    public String saveNewUser(@ModelAttribute("user") @Valid User user,
                              BindingResult result,
                              Model model) {

        List<Role> roles = roleService.getRoles();

        if (result.hasErrors()) {
            model.addAttribute("roles", roles);
            return "/admin/create-user";
        }

        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            result.rejectValue("username", "error.username", "Username already exists");
            model.addAttribute("roles", roles);
            return "/admin/create-user";
        }

        userService.saveUserWithEncodedPassword(user);

        return "redirect:/admin/users";
    }


    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllOrders(Model model){

        List<Order> allOrders = orderService.getAllOrders();
        Collections.sort(allOrders, Comparator.comparing(Order::getCreatedDate).reversed());

        model.addAttribute("orderList", allOrders);

        return "/user/order-history";
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getSingleOrder(@PathVariable UUID id, Model model){

        Optional<Order> order = orderService.findById(id);

        List<Status> statusList = Arrays.asList(Status.values());

        model.addAttribute("statusList", statusList);
        model.addAttribute("order", order.get());

//        model.addAttribute("orderItems", order.getOrderItems());

        return "user/single-order";
    }
}
