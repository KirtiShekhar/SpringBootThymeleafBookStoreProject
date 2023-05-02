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
@Table(name = "bookstore_application_user_billing_address")
public class UserBillingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_billing_address_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long userBillingAddressId;
    @Column(name = "user_billing_address_name", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingAddressName;
    @Column(name = "user_billing_address_street1", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingAddressStreet1;
    @Column(name = "user_billing_address_street2", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingAddressStreet2;
    @Column(name = "user_billing_address_city", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingAddressCity;
    @Column(name = "user_billing_address_state", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingAddressState;
    @Column(name = "user_billing_address_country", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingAddressCountry;
    @Column(name = "user_billing_address_zip_code", nullable = false, updatable = false, columnDefinition = "text")
    private String userBillingAddressZipCode;
    @OneToOne
    private UserOrder order;
}