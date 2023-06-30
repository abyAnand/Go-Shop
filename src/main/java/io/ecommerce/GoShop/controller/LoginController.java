package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.Auth.Otp.ILoginService;
import io.ecommerce.GoShop.Auth.login.OTPAuthenticationProvider;
import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.Auth.Otp.OtpService;
import io.ecommerce.GoShop.model.Banner;
import io.ecommerce.GoShop.model.LoginOtp;
import io.ecommerce.GoShop.model.OtpDto;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.banner.BannerService;
import io.ecommerce.GoShop.service.user.UserInfoDetailsService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    ILoginService loginService;

    @Autowired
    UserService userService;

    @Autowired
    OtpService otpService;


    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    BannerService bannerService;




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

    @GetMapping(value = {"/", "/index"})
    public String homePage(Model model) {

        List<Banner> banners = bannerService.findAll();
        model.addAttribute("banners", banners);

        return "index";
    }

    @GetMapping(value = {"/register", "/signup"})
    public String register(Model model) {

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

    @PostMapping("/user/verify-login-otp")
    @ResponseBody
    public String verifyOTPLogin(@RequestParam("username") String username,
                                 @RequestParam("otp") String otp,
                                 HttpServletRequest request) {
        boolean otpVerified = false;

        Optional<LoginOtp> loginOtp = loginService.findByusername(username);

        if (loginOtp.isPresent()) {
            if(loginOtp.get().getOtp().equals(otp)){
                otpVerified = true;
            }
        }

        if (otpVerified) {
            // OTP verification successful
            // Authenticate the user
            authenticateUser(username, request);
            return "success";
        } else {
            // OTP verification failed
            return "failure";
        }
    }

    private AuthenticationProvider otpAuthenticationProvider() {
        return new OTPAuthenticationProvider(userDetailsService, userService, otpService);
    }

    public UserDetailsService userDetailsService() {
        return new UserInfoDetailsService();
    }

    private void authenticateUser(String username, HttpServletRequest request) {
        List<GrantedAuthority> authorities;

        Optional<User> user = userService.findByUsername(username);

        authorities = Arrays.stream(user.get().getRole().getRoleName().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, authorities);
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = otpAuthenticationProvider().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @GetMapping("/otp-login")
    public String otpLogin() {
        return "otp-login";
    }

}
