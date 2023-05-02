package com.springboot.thymeleaf.bookstore.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookstore_application_users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "integer")
    private Long userId;
    @Column(name = "user_name", nullable = false, updatable = false, columnDefinition = "varchar(100)")
    private String userName;
    @Column(name = "user_password", nullable = false, updatable = false, columnDefinition = "text")
    private String userPassword;
    @Column(name = "user_first_name", nullable = false, updatable = false, columnDefinition = "varchar(100)")
    private String userFirstName;
    @Column(name = "user_last_name", nullable = false, updatable = false, columnDefinition = "varchar(100)")
    private String userLastName;
    @Column(name = "user_email_address", nullable = false, updatable = false, columnDefinition = "text")
    private String userEmailAddress;
    @Column(name = "user_contact_number", nullable = false, updatable = false, columnDefinition = "varchar(100)")
    private String userContactNumber;
    @Column(name = "user_is_enabled", nullable = false, updatable = false)
    private Boolean enabled = true;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private ShoppingCart shoppingCart;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserShipping> userShippingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserPayment> userPaymentList;
    @OneToMany(mappedBy = "user")
    private List<UserOrder> userOrderList;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Roles> userRoles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(uroles -> authorities.add(new UserAuthority(uroles.getUserRole().getUserRoleName())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
