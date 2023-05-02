package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.*;

public interface AddressAndPaymentService {
    UserShippingAddress setByUserShipping(UserShipping userShipping, UserShippingAddress userShippingAddress);

    UserBillingAddress setByUserBilling(UserBilling userBilling, UserBillingAddress userBillingAddress);

    Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
