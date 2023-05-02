package com.springboot.thymeleaf.bookstore.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookstore_application_cart_items")
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_items_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long cartItemId;
    @Column(name = "item_quantity", nullable = false, updatable = false, columnDefinition = "int")
    private int itemQuantity;
    @Column(name = "item_subtotal", nullable = false, updatable = false)
    private BigDecimal subtotal;
    @OneToOne
    private Book book;
    @OneToMany(mappedBy = "cartItem")
    @JsonIgnore
    private List<BookToCartItem> bookToCartItemList;
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private UserOrder order;
}
