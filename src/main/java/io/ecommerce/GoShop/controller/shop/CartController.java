package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.DTO.CouponResponse;
import io.ecommerce.GoShop.DTO.DeleteCartItemRequest;
import io.ecommerce.GoShop.DTO.ReviewResponse;
import io.ecommerce.GoShop.model.*;
import io.ecommerce.GoShop.repository.CartItemRepository;
import io.ecommerce.GoShop.repository.VariantRepository;
import io.ecommerce.GoShop.service.address.AddressService;
import io.ecommerce.GoShop.service.cart.ICartService;
import io.ecommerce.GoShop.service.coupon.CouponService;
import io.ecommerce.GoShop.service.order.OrderService;
import io.ecommerce.GoShop.service.product.ProductService;
import io.ecommerce.GoShop.service.user.UserService;
import io.ecommerce.GoShop.service.variant.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.stream.Collectors;


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

    @Autowired
    private CouponService couponService;

    @Autowired
    private ProductService productService;


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

        List<CartItem> cart = userCart.map(Cart::getCartItems).orElse(Collections.emptyList());
        List<CartItem> filteredCart = cart.stream()
                .filter(cartItem -> cartItem.getQuantity() != 0)
                .collect(Collectors.toList());

        model.addAttribute("cart", filteredCart);
        model.addAttribute("name", name);

        return "cart";
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> addToCart(@RequestParam("variant") Variant variant) {
        Optional<User> user = userService.findByUsername(getCurrentUsername());
        Optional<Cart> userCart = user.map(cartService::getCart).orElse(null);

        if (variant.getStock() > 0) {
            CartItem cartItem = new CartItem(variant, 1, userCart.get());
            variantService.decreaseQuantity(variant);
            cartService.addToCartList(cartItem);
            String message = "Item added to cart.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "Item is not in stock.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }


    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> deleteFromCart(@RequestBody DeleteCartItemRequest request) {

        String variantId = request.getVariantId();
        int quantity = request.getQuantity();

        Optional<User> user = userService.findByUsername(getCurrentUsername());
        Cart userCart = user.flatMap(cartService::getCart).orElse(null);

        UUID variantUUID = UUID.fromString(variantId);
        Variant variant = variantService.findById(variantUUID).orElse(null);

        if (variant != null) {
            variantService.addQuantity(variant, quantity);
        }
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

        Optional<Variant> variant = variantService.findById(UUID.fromString(variantId));
        if (variant.isPresent()) {
            Variant selectedVariant = variant.get();
            if (selectedVariant.getStock() > 0) {
                cartService.increaseQuantity(UUID.fromString(variantId));
                variantService.decreaseQuantity(selectedVariant);
                // Return a success response
                String successMessage = "Cart item increased successfully";
                return ResponseEntity.ok(successMessage);
            } else {
                // Return an error response
                String errorMessage = "Item is out of stock.";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
        } else {
            // Return an error response if variant not found
            String errorMessage = "Variant not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping("/decrease")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> decreaseCartItem(@RequestBody String variantId) {
        cartService.decreaseQuantity(UUID.fromString(variantId));

        Variant variant = variantService.findById(UUID.fromString(variantId)).orElse(null);

        variantService.addQuantity(variant);
        // Return a success response
        String successMessage = "Cart item decreased successfully";
        return ResponseEntity.ok(successMessage);
    }

    @PostMapping("/apply-coupon")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<?> applyCoupon(@RequestBody String couponCode) {

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);
        Optional<Cart> cart = Optional.ofNullable(cartService.findByUser(user));
        Optional<Coupon> coupon = couponService.findByCode(couponCode);
        CouponResponse response = new CouponResponse();

        System.out.println(couponCode);

        if (cart.isPresent()) {
            boolean isApplicable = cart.get().getCartItems().stream()
                    .anyMatch(item -> item.getVariant().getProduct().equals(coupon.get().getProduct())
                            || item.getVariant().getProduct().getCategory().equals(coupon.get().getCategory()));

            response.setApplicable(isApplicable);

        }

        if (coupon.isPresent()) {
            response.setValid(true);
            response.setProductSpecific(coupon.get().getProduct() != null);
            response.setCategorySpecific(coupon.get().getCategory() != null);
            response.setDiscountPercentage(coupon.get().getDiscount());
            cart.get().setCoupon(coupon.get());
        } else {
            response.setValid(false);
        }

        return ResponseEntity.ok(response);
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

        double discount = 0.0;

        if (cart.getCoupon() != null) {
            Coupon coupon = cart.getCoupon();
            coupon.setCouponStock(coupon.getCouponStock()-1);
            couponService.save(coupon);

            if (coupon.getType() == CouponType.GENERAL) {
                double maxDiscount = coupon.getMaximumDiscountAmount();
                discount = total * ((double) coupon.getDiscount() / 100);
                if (discount > maxDiscount) {
                    discount = maxDiscount;
                }
                model.addAttribute("couponType", "General");
            } else if (coupon.getType() == CouponType.CATEGORY) {
                double categoryTotal = cartItems.stream()
                        .filter(cartItem -> cartItem.getVariant().getProduct().getCategory().equals(coupon.getCategory()))
                        .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
                        .sum();

                double maxDiscount = coupon.getMaximumDiscountAmount();
                discount = categoryTotal * ((double) coupon.getDiscount() / 100);
                if (discount > maxDiscount) {
                    discount = maxDiscount;
                }
                model.addAttribute("couponType", "Category");
                model.addAttribute("couponCategory", coupon.getCategory());
            } else if (coupon.getType() == CouponType.PRODUCT) {
                double productTotal = cartItems.stream()
                        .filter(cartItem -> cartItem.getVariant().getProduct().equals(coupon.getProduct()))
                        .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
                        .sum();

                double maxDiscount = coupon.getMaximumDiscountAmount();
                discount = productTotal * ((double) coupon.getDiscount() / 100);
                if (discount > maxDiscount) {
                    discount = maxDiscount;
                }
                model.addAttribute("couponType", "Product");
                model.addAttribute("couponProduct", coupon.getProduct());
            }

            String formattedDiscount = String.format("%.2f", discount);
            model.addAttribute("discount", formattedDiscount);
            model.addAttribute("couponApplied", true);
        } else {
            model.addAttribute("couponApplied", false);
        }

        System.out.println("Original Total: $" + total);
        System.out.println("Total After Coupon Discount: $" + (total > 0 ? total : 0));

        int payment = cart.getPayment().ordinal();

        if(payment == 0){
            model.addAttribute("shipping", 40);
        }

        model.addAttribute("cartItems",cartItems);
        model.addAttribute("subtotal",total);


        return "checkout";
    }

    @PostMapping("/product/review")
    public ResponseEntity<String> addProductReview(@RequestBody ReviewResponse reviewResponse) {
        UUID productId = reviewResponse.getProductId();
        int rating = reviewResponse.getRating();
        String review = reviewResponse.getReview();

        // Retrieve the product from the database
        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            String errorMessage = "Product not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        System.out.println(reviewResponse);

        // Assuming the review is successfully added, you can return a success message
        String response = "Review added successfully";
        return ResponseEntity.ok(response);
    }


    @PostMapping("/payment")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String payment(@RequestBody String paymentOption) {

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);
        Cart cart = cartService.getCart(user).orElse(null);

        assert cart != null;
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

        assert cart != null;
        if(cart.getCartItems().isEmpty()){
            return "index";
        }

        assert user != null;
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

        double discount = 0.0;
        List<CartItem> cartItems = cart.getCartItems();

        if (cart.getCoupon() != null) {
            Coupon coupon = cart.getCoupon();
            order.setCoupon(coupon);

            if (coupon.getType() == CouponType.GENERAL) {
                discount = total * ((double) coupon.getDiscount() / 100);
                System.out.println(discount);
            } else if (coupon.getType() == CouponType.CATEGORY) {
                double categoryTotal = cartItems.stream()
                        .filter(cartItem -> cartItem.getVariant().getProduct().getCategory().equals(coupon.getCategory()))
                        .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
                        .sum();

                discount = categoryTotal * ((double) coupon.getDiscount() / 100);
            } else if (coupon.getType() == CouponType.PRODUCT) {
                double productTotal = cartItems.stream()
                        .filter(cartItem -> cartItem.getVariant().getProduct().equals(coupon.getProduct()))
                        .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
                        .sum();

                discount = productTotal * ((double) coupon.getDiscount() / 100);

            }

            double discountedAmount = total - discount;

        }

        if(cart.getPayment().equals(Payment.COD)){
            total += 40;
        }

        order.setTotal((float) (total-discount));
        orderService.saveOrder(order);
        userService.deleteCart(cart);
        cartService.deleteCart(cart);
        HttpSession session = request.getSession();
        session.setAttribute("order", order.getId());

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


        //TODO: show the coupon info and how much availed in the page

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
