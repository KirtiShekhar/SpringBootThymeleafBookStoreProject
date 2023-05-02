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
@Table(name = "bookstore_application_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long paymentId;
    @Column(name = "payment_type", nullable = false, updatable = false, columnDefinition = "text")
    private String paymentType;
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
    @OneToOne
    private UserOrder order;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userPayment")
    private UserBilling userBilling;
}