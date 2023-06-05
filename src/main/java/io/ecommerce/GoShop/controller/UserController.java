package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;



//    @GetMapping(value = {"/register","/signup"})
//    public String register(Model model){
//
//        UserDTO user = new UserDTO();
//
//        model.addAttribute("user", user);
//        return "user/register";
//    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserDTO user, BindingResult result, Model model){
        return "index";
    }


    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute UserDTO user,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "user/register";
        } else {
            Optional<User> existingUser = userService.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                result.rejectValue("username", "error.username", "Username already exists");
                model.addAttribute("user", user);
                model.addAttribute("errorMsg", "Username already exists");
                return "user/register";
            }
            userService.save(user);
        }

        return "index";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable UUID id, Model model){

        User user = userService.findById(id);

        return "index";
    }

    @GetMapping("/category")
    public String shop(){
        return "category";
    }

}
