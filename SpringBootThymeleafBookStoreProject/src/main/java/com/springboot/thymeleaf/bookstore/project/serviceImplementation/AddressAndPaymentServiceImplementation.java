package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.*;
import com.springboot.thymeleaf.bookstore.project.service.AddressAndPaymentService;
import org.springframework.stereotype.Service;

@Service
public class AddressAndPaymentServiceImplementation implements AddressAndPaymentService {
    @Override
    public UserShippingAddress setByUserShipping(UserShipping userShipping, UserShippingAddress userShippingAddress) {
        userShippingAddress.setUserShippingAddressName(userShipping.getUserShippingName());
        userShippingAddress.setUserShippingAddressStreet1(userShipping.getUserShippingStreet1());
        userShippingAddress.setUserShippingAddressStreet2(userShipping.getUserShippingStreet2());
        userShippingAddress.setUserShippingAddressCity(userShipping.getUserShippingCity());
        userShippingAddress.setUserShippingAddressState(userShipping.getUserShippingState());
        userShippingAddress.setUserShippingAddressCountry(userShipping.getUserShippingCountry());
        userShippingAddress.setUserShippingAddressZipCode(userShipping.getUserShippingZipCode());
        return userShippingAddress;
    }

    @Override
    public UserBillingAddress setByUserBilling(UserBilling userBilling, UserBillingAddress userBillingAddress) {
        userBillingAddress.setUserBillingAddressName(userBilling.getUserBillingName());
        userBillingAddress.setUserBillingAddressStreet1(userBilling.getUserBillingStreet1());
        userBillingAddress.setUserBillingAddressStreet2(userBilling.getUserBillingStreet2());
        userBillingAddress.setUserBillingAddressCity(userBilling.getUserBillingCity());
        userBillingAddress.setUserBillingAddressState(userBilling.getUserBillingState());
        userBillingAddress.setUserBillingAddressCountry(userBilling.getUserBillingCountry());
        userBillingAddress.setUserBillingAddressZipCode(userBilling.getUserBillingZipCode());
        return userBillingAddress;
    }

    @Override
    public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
        payment.setPaymentType(userPayment.getUserPaymentType());
        payment.setCardHolderName(userPayment.getCardHolderName());
        payment.setCardName(userPayment.getCardName());
        payment.setCardNumber(userPayment.getCardNumber());
        payment.setCardExpiryMonth(userPayment.getCardExpiryMonth());
        payment.setCardExpiryYear(userPayment.getCardExpiryYear());
        payment.setCardCvc(userPayment.getCardCvc());
        return payment;
    }
}
