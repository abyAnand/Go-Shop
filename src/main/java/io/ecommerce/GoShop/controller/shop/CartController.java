package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.CartItem;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Variant;
import io.ecommerce.GoShop.service.cart.ICartService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;


@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    UserService userService;

    @Autowired
    ICartService cartService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getCartList(Model model){

        String name = getCurrentUsername();
        Optional<User> user = userService.findByUsername(name);
        Optional<Cart> userCart = user.map(cartService::getCart).orElse(null);

        model.addAttribute("cartItems", userCart.map(Cart::getCartItems).orElse(null));
        model.addAttribute("name", name);

        return "cart";
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> addToCart(@RequestParam("variant") Variant variant) {
        // Add the logic to add the variant to the cart

        Optional<User> user = userService.findByUsername(getCurrentUsername());
        Optional<Cart> userCart = user.map(cartService::getCart).orElse(null);

        CartItem cartItem = new CartItem(variant, 1, userCart.get());
        cartService.addToCartList(cartItem);

        String message = "added";
        return ResponseEntity.ok(message);
    }

}
