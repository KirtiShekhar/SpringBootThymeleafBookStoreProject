package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.UserShipping;

public interface UserShippingService {
    UserShipping findByUserShippingId(Long userShippingId);

    void removeByUserShippingId(Long userShippingId);
}
