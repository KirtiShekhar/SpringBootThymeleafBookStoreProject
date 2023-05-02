package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.*;

import javax.transaction.Transactional;
import java.util.Set;

public interface UserService {
    User findUserByUserName(String userName);

    PasswordResetToken getPasswordResetToken(String passwordResetToken);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    User findByUserId(Long userId);

    User findByUserEmailAddress(String userEmailAddress);

    User findByUserContactNumber(String userContactNumber);

    @Transactional
    User createUser(User user, Set<Roles> userRoles);
    

    User saveUser(User user);

    void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

    void updateUserShipping(UserShipping userShipping, User user);

    void setUserDefaultPayment(Long userPaymentId, User user);

    void setUserDefaultShipping(Long userShippingId, User user);
}
