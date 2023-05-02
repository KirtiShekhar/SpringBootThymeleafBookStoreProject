package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.UserPayment;
import com.springboot.thymeleaf.bookstore.project.repository.UserPaymentRepository;
import com.springboot.thymeleaf.bookstore.project.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPaymentServiceImplementation implements UserPaymentService {
    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Override
    public UserPayment findById(Long userPaymentId) {
        return userPaymentRepository.findById(userPaymentId).get();
    }

    @Override
    public void removeById(Long userPaymentId) {
        userPaymentRepository.deleteById(userPaymentId);
    }
}
