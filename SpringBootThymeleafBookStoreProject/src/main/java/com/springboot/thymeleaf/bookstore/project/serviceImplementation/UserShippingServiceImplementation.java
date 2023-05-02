package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.UserShipping;
import com.springboot.thymeleaf.bookstore.project.repository.UserShippingRepository;
import com.springboot.thymeleaf.bookstore.project.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingServiceImplementation implements UserShippingService {
    @Autowired
    private UserShippingRepository userShippingRepository;

    @Override
    public UserShipping findByUserShippingId(Long userShippingId) {
        return userShippingRepository.findById(userShippingId).get();
    }

    @Override
    public void removeByUserShippingId(Long userShippingId) {
        userShippingRepository.deleteById(userShippingId);
    }
}
