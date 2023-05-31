package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/")
    public String homePage(){
        return "index";
    }


    @GetMapping(value = {"/register","/signup"})
    public String register(Model model){

        UserDTO user = new UserDTO();

        model.addAttribute("user", user);
        return "user/register";
    }

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
                           Model model){



        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "user/register";
        }else{
            Optional<User> existingUser = userService.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                result.rejectValue("username", "error.user", "Username already exists");
                model.addAttribute("user", user);
                return "user/register";
            }
            userService.save(user);
        }

        return "index";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable UUID id, RedirectAttributes attributes, Model model){

        User user = userService.findById(id);

        return "index";
    }

}
