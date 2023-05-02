package com.springboot.thymeleaf.bookstore.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookstore_application_user_order")
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_order_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long userOrderId;
    @Column(name = "user_order_date", nullable = false, updatable = false, columnDefinition = "date")
    private Date userOrderDate;
    @Column(name = "order_shipping_date", nullable = false, updatable = false, columnDefinition = "date")
    private Date orderShippingDate;
    @Column(name = "order_shipping_method", nullable = false, updatable = false, columnDefinition = "text")
    private String orderShippingMethod;
    @Column(name = "user_order_status", nullable = false, updatable = false, columnDefinition = "text")
    private String userOrderStatus;
    @Column(name = "user_order_total", nullable = false, updatable = false)
    private BigDecimal userOrderTotal;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItems> cartItemsList;
    @OneToOne(cascade = CascadeType.ALL)
    private UserShippingAddress shippingAddress;
    @OneToOne(cascade = CascadeType.ALL)
    private UserBillingAddress billingAddress;
    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;
    @ManyToOne
    private User user;
}