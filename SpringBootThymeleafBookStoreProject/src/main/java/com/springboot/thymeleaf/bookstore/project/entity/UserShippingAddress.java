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
@Table(name = "bookstore_application_user_shipping_address")
public class UserShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_shipping_address_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long userShippingAddressId;
    @Column(name = "user_shipping_address_name", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingAddressName;
    @Column(name = "user_shipping_address_street1", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingAddressStreet1;
    @Column(name = "user_shipping_address_street2", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingAddressStreet2;
    @Column(name = "user_shipping_address_city", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingAddressCity;
    @Column(name = "user_shipping_address_state", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingAddressState;
    @Column(name = "user_shipping_address_country", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingAddressCountry;
    @Column(name = "user_shipping_address_zip_code", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingAddressZipCode;
    @OneToOne
    private UserOrder order;
}