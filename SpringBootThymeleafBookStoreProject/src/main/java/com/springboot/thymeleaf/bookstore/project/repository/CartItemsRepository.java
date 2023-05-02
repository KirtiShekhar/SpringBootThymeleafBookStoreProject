package com.springboot.thymeleaf.bookstore.project.repository;

import com.springboot.thymeleaf.bookstore.project.entity.CartItems;
import com.springboot.thymeleaf.bookstore.project.entity.ShoppingCart;
import com.springboot.thymeleaf.bookstore.project.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    List<CartItems> findByShoppingCart(ShoppingCart shoppingCart);

    List<CartItems> findByOrder(UserOrder order);
}