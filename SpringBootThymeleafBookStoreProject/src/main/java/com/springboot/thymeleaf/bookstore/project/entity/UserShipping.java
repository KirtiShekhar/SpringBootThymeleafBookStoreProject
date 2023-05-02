package com.springboot.thymeleaf.bookstore.project.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "bookstore_application_user_shipping")
public class UserShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_shipping_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long userShippingId;
    @Column(name = "user_shipping_name", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingName;
    @Column(name = "user_shipping_street1", nullable = false, updatable = false, columnDefinition = "text")
    @NonNull
    private String userShippingStreet1;
    @Column(name = "user_shipping_street2", nullable = false, updatable = false, columnDefinition = "text")
    @NonNull
    private String userShippingStreet2;
    @Column(name = "user_shipping_city", nullable = false, updatable = false, columnDefinition = "text")
    @NonNull
    private String userShippingCity;
    @Column(name = "user_shipping_state", nullable = false, updatable = false, columnDefinition = "text")
    @NonNull
    private String userShippingState;
    @Column(name = "user_shipping_country", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingCountry;
    @Column(name = "user_shipping_zip_code", nullable = false, updatable = false, columnDefinition = "text")
    private String userShippingZipCode;
    @Column(name = "user_shipping_default", nullable = false, updatable = false, columnDefinition = "BIT(1)")
    private boolean userShippingDefault;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
