package io.ecommerce.GoShop.service.cart;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.CartItem;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.CartItemRepository;
import io.ecommerce.GoShop.repository.CartRepository;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserService userService;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }


    @Override
    public Optional<Cart> getCart(User user) {
        return cartRepository.getCartByUser(user)
                .or(() -> {
                    Cart newCart = createCart(user);
                    user.setCart(newCart);
                    cartRepository.save(newCart);
                    return Optional.of(newCart);
                });

    }

    @Override
    public Cart createCart(User user) {

        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }


    @Override
    public void addToCartList(CartItem item) {

        User user = item.getCart().getUser();
        Cart cart = cartRepository.getCartByUser(user)
                .orElseGet(() -> createCart(user));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getVariant().equals(item.getVariant()))
                .findFirst();

        if (existingItem.isPresent()) {
            updateCartItem(existingItem.get(), existingItem.get().getQuantity() + 1);
        } else {
            item.setQuantity(1);
            cart.getCartItems().add(item);
            cartItemRepository.save(item);
        }

        cartRepository.save(cart);
    }

    @Override
    public void updateCartItem(CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeFromCartList(CartItem item) {
        User user = item.getCart().getUser();
        Cart cart = cartRepository.getCartByUser(user)
                .orElseGet(() -> createCart(user));



        boolean removed = cart.getCartItems().removeIf(cartItem -> cartItem.equals(item));

        if (removed) {
            cartItemRepository.delete(item);
            cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("Item not found in cart");
        }
    }

    public void increaseQuantity(UUID itemId) {
        User user = userService.findByUsername(getCurrentUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.getCartByUser(user).orElseGet(() -> createCart(user));

        CartItem item = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getVariant().getId().equals(itemId))
                .findFirst()
                .orElse(null);

        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
    }

    @Override
    public void decreaseQuantity(UUID itemId) {
        User user = userService.findByUsername(getCurrentUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.getCartByUser(user).orElseGet(() -> createCart(user));

        CartItem item = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getVariant().getId().equals(itemId))
                .findFirst()
                .orElse(null);

        int newQuantity = item.getQuantity() - 1;
        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }

    }

    public void decreaseQuantity(CartItem item) {
        int newQuantity = item.getQuantity() - 1;
        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }
    }
}
