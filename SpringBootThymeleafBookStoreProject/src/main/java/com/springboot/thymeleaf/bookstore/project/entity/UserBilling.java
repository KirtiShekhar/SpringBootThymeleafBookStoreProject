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
@Table(name = "bookstore_application_user_billing")
public class UserBilling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_billing_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long userBillingId;
    @Column(name = "user_billing_name", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingName;
    @Column(name = "user_billing_street1", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingStreet1;
    @Column(name = "user_billing_street2", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingStreet2;
    @Column(name = "user_billing_city", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingCity;
    @Column(name = "user_billing_state", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingState;
    @Column(name = "user_billing_country", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingCountry;
    @Column(name = "user_billing_zip_code", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingZipCode;
    @OneToOne(cascade = CascadeType.ALL)
    private UserPayment userPayment;
}
