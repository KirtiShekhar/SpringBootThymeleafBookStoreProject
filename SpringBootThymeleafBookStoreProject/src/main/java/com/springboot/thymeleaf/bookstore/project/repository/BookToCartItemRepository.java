package com.springboot.thymeleaf.bookstore.project.repository;

import com.springboot.thymeleaf.bookstore.project.entity.BookToCartItem;
import com.springboot.thymeleaf.bookstore.project.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {
    void deleteByCartItem(CartItems cartItem);
}