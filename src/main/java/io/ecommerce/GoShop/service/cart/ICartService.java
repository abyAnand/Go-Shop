package io.ecommerce.GoShop.service.cart;

import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.CartItem;
import io.ecommerce.GoShop.model.User;

import java.util.Optional;
import java.util.UUID;

public interface ICartService {

    Optional<Cart> getCart(User user);

    Cart createCart(User user);

    void addToCartList(CartItem item);

    void updateCartItem(CartItem item, int quantity);

    void removeFromCartList(CartItem item);

    void increaseQuantity(UUID itemId);

    void decreaseQuantity(UUID itemId);
}