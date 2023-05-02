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
@Table(name = "bookstore_application_user_payment")
public class UserPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_payment_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long userPaymentId;
    @Column(name = "user_payment_type", nullable = false, updatable = false, columnDefinition = "text")
    private String userPaymentType;
    @Column(name = "card_name", nullable = false, updatable = false, columnDefinition = "text")
    private String cardName;
    @Column(name = "card_number", nullable = false, updatable = false, columnDefinition = "text")
    private String cardNumber;
    @Column(name = "card_expiry_month", nullable = false, updatable = false, columnDefinition = "int")
    private int cardExpiryMonth;
    @Column(name = "card_expiry_year", nullable = false, updatable = false, columnDefinition = "int")
    private int cardExpiryYear;
    @Column(name = "card_cvc", nullable = false, updatable = false, columnDefinition = "int")
    private int cardCvc;
    @Column(name = "card_holder_name", nullable = false, updatable = false, columnDefinition = "text")
    private String cardHolderName;
    @Column(name = "default_payment", nullable = false, updatable = false, columnDefinition = "BIT(1)")
    private boolean defaultPayment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userPayment")
    private UserBilling userBilling;
}
