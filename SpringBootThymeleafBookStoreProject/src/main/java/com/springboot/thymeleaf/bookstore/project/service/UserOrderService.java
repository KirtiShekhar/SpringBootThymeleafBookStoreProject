package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.*;

public interface UserOrderService {
    UserOrder createNewUserOrder(ShoppingCart shoppingCart, UserShippingAddress userShippingAddress, UserBillingAddress userBillingAddress, Payment payment, String orderShippingMethod, User user);

    UserOrder findOneOrder(Long userOrderId);
}
