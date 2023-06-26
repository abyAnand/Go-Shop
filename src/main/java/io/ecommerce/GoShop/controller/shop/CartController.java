package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.model.*;
import io.ecommerce.GoShop.repository.CartItemRepository;
import io.ecommerce.GoShop.repository.VariantRepository;
import io.ecommerce.GoShop.service.address.AddressService;
import io.ecommerce.GoShop.service.cart.ICartService;
import io.ecommerce.GoShop.service.order.OrderService;
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


import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private AddressService addressService;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderService orderService;


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

    @GetMapping("/checkout")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String checkOut(Model model){

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);

        List<Address> addresses = addressService.findByUser(user);

        model.addAttribute("addresses", addresses);
        Cart cart = cartService.findByUser(user);

        if(cart == null){
            return "cart";
        }

        List<CartItem> cartItems = cart.getCartItems();

        double total = cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
                .sum();

        int payment = cart.getPayment().ordinal();

        if(payment == 0){
            model.addAttribute("shipping", 40);
        }

        model.addAttribute("cartItems",cartItems);
        model.addAttribute("subtotal",total);

        return "checkout";
    }


    @PostMapping("/payment")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String payment(@RequestBody String paymentOption) {

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);

        Cart cart = cartService.getCart(user).orElse(null);

        System.out.println(paymentOption);

        if(paymentOption.equals("cod")){
            cart.setPayment(Payment.COD);
        }else if(paymentOption.equals("online_pay")){
            cart.setPayment(Payment.ONLINE);
        }
        return "index";
    }

    @PostMapping("/buy")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String checkout(@RequestBody String addressId, HttpServletRequest request) {

        UUID addressUUID = UUID.fromString(addressId);

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);
        Cart cart = cartService.getCart(user).orElse(null);

        if(cart.getCartItems().isEmpty()){
            return "index";
        }

        Address address = user.getAddresses()
                .stream()
                .filter(addr -> addr.getId().equals(addressUUID))
                .findFirst()
                .orElse(null);

        Order order = new Order();
        order.setAddress(address);
        order.setUser(user);

        List<OrderItem> orderItemList = new ArrayList<>();

        for(CartItem cartItem : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setVariant(cartItem.getVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getVariant().getPrice());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }
        order.setOrderItems(orderItemList);
        order.setPayment(cart.getPayment());
        order.setStatus(Status.CONFIRMED);

        double total = cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getVariant().getOfferPrice() * cartItem.getQuantity())
                .sum();

        if(cart.getPayment().equals(Payment.COD)){
            total += 40;
        }

        order.setTotal((float) total);

        orderService.saveOrder(order);


        userService.deleteCart(cart);
      //  cart = cartService.removeUser(cart);
        cartService.deleteCart(cart);
        HttpSession session = request.getSession();
        session.setAttribute("order", order.getId());

      //  cartService.deleteCartItems(cart);


        return "index";
    }

    @GetMapping("/confirmation")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String confirmation(HttpSession session, Model model){
        UUID orderId = (UUID) session.getAttribute("order");
        System.out.println(orderId);

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);
        Order order = orderService.findById(orderId).orElse(null);

        model.addAttribute("orderItems", order.getOrderItems());

        if(order.getPayment().equals(Payment.COD)){
            model.addAttribute("subtotal", order.getTotal()-40);
            model.addAttribute("shipping", 40);
        }

        model.addAttribute("total", order.getTotal());
        model.addAttribute("address", order.getAddress());
        model.addAttribute("order", order);
        Timestamp createdDate = order.getCreatedDate();
        Date date = new Date(createdDate.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        model.addAttribute("createdDate", formattedDate);


        return "confirmation";
    }


}
