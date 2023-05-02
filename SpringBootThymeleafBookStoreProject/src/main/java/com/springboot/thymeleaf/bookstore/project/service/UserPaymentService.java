package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.UserPayment;

public interface UserPaymentService {
    UserPayment findById(Long userPaymentId);

    void removeById(Long userPaymentId);
}
