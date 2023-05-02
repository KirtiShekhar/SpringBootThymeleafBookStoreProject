package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.*;

import java.util.List;

public interface CartItemService {
    List<CartItems> findByShoppingCart(ShoppingCart shoppingCart);

    CartItems updateCartItems(CartItems cartItems);

    CartItems addBookToCartItem(Book book, User user, int itemQuantity);

    CartItems findByCartItemId(Long cartItemId);

    void removeCartItems(CartItems cartItems);

    CartItems saveCartItems(CartItems cartItems);

    List<CartItems> findByUserOrder(UserOrder userOrder);
}
