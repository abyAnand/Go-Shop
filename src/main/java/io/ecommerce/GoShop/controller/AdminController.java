package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public String userList(Model model){

        List<User> userList= userService.findAll();
        model.addAttribute("users",userList);

        return "/admin/user-management";
    }
}
