package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.Auth.Otp.OtpService;
import io.ecommerce.GoShop.model.OtpDto;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    OtpService otpService;

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

    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<String> verifyOTP(@RequestParam("otp") String otp, @RequestParam("sessionId") String sessionId) {
        Optional<OtpDto> session = otpService.findBySessionId(sessionId);

        if (session.isPresent()) {
            if (session.get().getOtp().equals(otp)) {
                return ResponseEntity.ok("success"); // OTP verification successful
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failure"); // OTP verification failed
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failure");
    }

}
