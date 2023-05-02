package com.springboot.thymeleaf.bookstore.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookstore_application_book_to_cart_items")
public class BookToCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_to_cart_items_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long bookToCartItemId;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "cart_items_id")
    private CartItems cartItem;

}