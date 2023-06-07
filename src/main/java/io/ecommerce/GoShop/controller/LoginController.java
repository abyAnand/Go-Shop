package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
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

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping(value = {"/","/index"})
    public String homePage(){
        return "index";
    }

    @GetMapping(value = {"/register","/signup"})
    public String register(Model model){

        UserDTO user = new UserDTO();

        model.addAttribute("user", user);
        return "user/register";
    }

    @PostMapping("/user/check-username")
    @ResponseBody
    public String checkUsernameAvailability(@RequestParam("username") String username) {
        Optional<User> existingUser = userService.findByUsername(username);
        return existingUser.isPresent() ? "taken" : "available";
    }

    @PostMapping("/user/check-email")
    @ResponseBody
    public String checkEmailAvailability(@RequestParam("email") String email) {
        Optional<User> existingUser = userService.findByEmail(email);
        return existingUser.isPresent() ? "taken" : "available";
    }

    @PostMapping("/user/check-phone")
    @ResponseBody
    public String checkPhoneNumberAvailability(@RequestParam("phoneNumber") long phoneNumber) {
        Optional<User> existingUser = userService.findByPhoneNumber(phoneNumber);
        return existingUser.isPresent() ? "taken" : "available";
    }

}
