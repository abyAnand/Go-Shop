package io.ecommerce.GoShop.service.cart;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.CartItem;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.CartItemRepository;
import io.ecommerce.GoShop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService{

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;
    @Override
    public Optional<Cart> getCart(User user) {
        Optional<Cart> cart = cartRepository.getCartByUser(user);
        cart = cart.or(() -> {
            Cart newCart = createCart(user);
            user.setCart(newCart);
            cartRepository.save(newCart);  // Save the new cart to the repository
            return Optional.of(newCart);
        });
        return cart;
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
            updateCartItem(existingItem.get(), existingItem.get().getQuantity()+1);
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

    }
}
