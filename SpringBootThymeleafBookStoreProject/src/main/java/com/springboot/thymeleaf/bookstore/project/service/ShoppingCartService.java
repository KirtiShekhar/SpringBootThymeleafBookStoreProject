package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

    void clearShoppingCart(ShoppingCart shoppingCart);
}
