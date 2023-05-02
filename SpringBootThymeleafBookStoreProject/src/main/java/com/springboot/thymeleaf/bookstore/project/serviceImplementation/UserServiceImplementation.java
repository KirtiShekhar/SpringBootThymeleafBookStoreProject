package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.*;
import com.springboot.thymeleaf.bookstore.project.repository.*;
import com.springboot.thymeleaf.bookstore.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private UserShippingRepository userShippingRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String passwordResetToken) {
        return passwordResetTokenRepository.findByPasswordResetToken(passwordResetToken);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String passwordResetToken) {
        final PasswordResetToken resetToken = new PasswordResetToken(passwordResetToken, user);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public User findByUserEmailAddress(String userEmailAddress) {
        return userRepository.findByUserEmailAddress(userEmailAddress);
    }

    @Override
    public User findByUserContactNumber(String userContactNumber) {
        return userRepository.findByUserContactNumber(userContactNumber);
    }

    @Override
    @Transactional
    public User createUser(User user, Set<Roles> userRoles) {
        User localUser = userRepository.findByUserName(user.getUsername());
        if (localUser == null) {
            for (Roles userRole : userRoles) {
                userRoleRepository.save(userRole.getUserRole());
            }
        } else {
            throw new RuntimeException("User Already exist " + localUser.getUsername());
        }
        user.getUserRoles().addAll(userRoles);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        user.setShoppingCart(shoppingCart);
        user.setUserShippingList(new ArrayList<UserShipping>());
        user.setUserPaymentList(new ArrayList<UserPayment>());
        localUser = userRepository.save(user);
        return localUser;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
        userPayment.setUser(user);
        userPayment.setUserBilling(userBilling);
        userPayment.setDefaultPayment(true);
        userBilling.setUserPayment(userPayment);
        user.getUserPaymentList().add(userPayment);
    }

    @Override
    public void updateUserShipping(UserShipping userShipping, User user) {
        userShipping.setUser(user);
        userShipping.setUserShippingDefault(true);
        user.getUserShippingList().add(userShipping);
    }

    @Override
    public void setUserDefaultPayment(Long userPaymentId, User user) {
        List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
        for (UserPayment userPayment : userPaymentList) {
            if (userPayment.getUserPaymentId().equals(userPaymentId)) {
                userPayment.setDefaultPayment(true);
                userPaymentRepository.save(userPayment);
            } else {
                userPayment.setDefaultPayment(false);
                userPaymentRepository.save(userPayment);
            }
        }
    }

    @Override
    public void setUserDefaultShipping(Long userShippingId, User user) {
        List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();
        for (UserShipping userShipping : userShippingList) {
            if (userShipping.getUserShippingId().equals(userShippingId)) {
                userShipping.setUserShippingDefault(true);
                userShippingRepository.save(userShipping);
            } else {
                userShipping.setUserShippingDefault(false);
                userShippingRepository.save(userShipping);
            }
        }
    }
}
