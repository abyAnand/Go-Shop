package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.CartItem;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Variant;
import io.ecommerce.GoShop.repository.VariantRepository;
import io.ecommerce.GoShop.service.cart.ICartService;
import io.ecommerce.GoShop.service.user.UserService;
import io.ecommerce.GoShop.service.variant.VariantService;
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
import java.util.UUID;


@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    UserService userService;

    @Autowired
    ICartService cartService;

    @Autowired
    VariantService variantService;
    @Autowired
    private VariantRepository variantRepository;

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
        Optional<Cart> userCart = user.flatMap(cartService::getCart);

        model.addAttribute("cart", userCart.map(Cart::getCartItems).orElse(null));
        model.addAttribute("name", name);

        return "cart";
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> addToCart(@RequestParam("variant") Variant variant) {


        Optional<User> user = userService.findByUsername(getCurrentUsername());
        Optional<Cart> userCart = user.map(cartService::getCart).orElse(null);

        CartItem cartItem = new CartItem(variant, 1, userCart.get());
        cartService.addToCartList(cartItem);

        String message = "added";
        return ResponseEntity.ok(message);
    }


    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> deleteFromCart(@RequestBody String variantId) {

        Optional<User> user = userService.findByUsername(getCurrentUsername());
        Cart userCart = user.flatMap(cartService::getCart).orElse(null);

        UUID variantUUID = UUID.fromString(variantId);

        assert userCart != null;
        userCart.getCartItems()
                .stream()
                .filter(item -> item.getVariant().getId().equals(variantUUID))
                .findFirst()
                .ifPresent(cartService::removeFromCartList);




        String successMessage = "Cart item deleted successfully";
        return ResponseEntity.ok(successMessage);
    }



    @PostMapping("/increase")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> increaseCartItem(@RequestBody String variantId) {

        cartService.increaseQuantity(UUID.fromString(variantId));

        // Return a success response
        String successMessage = "Cart item increased successfully";
        return ResponseEntity.ok(successMessage);
    }

    @PostMapping("/decrease")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> decreaseCartItem(@RequestBody String variantId) {
        cartService.decreaseQuantity(UUID.fromString(variantId));
        // Return a success response
        String successMessage = "Cart item decreased successfully";
        return ResponseEntity.ok(successMessage);
    }


}
