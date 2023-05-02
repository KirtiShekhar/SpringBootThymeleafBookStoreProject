package com.springboot.thymeleaf.bookstore.project.controller;

import com.springboot.thymeleaf.bookstore.project.entity.*;
import com.springboot.thymeleaf.bookstore.project.security.CustomUserDetailsService;
import com.springboot.thymeleaf.bookstore.project.service.*;
import com.springboot.thymeleaf.bookstore.project.utils.IndiaConstants;
import com.springboot.thymeleaf.bookstore.project.utils.ResetMailConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/web/bookStore/checkout")
public class CheckOutController {
    private Logger bookStoreLogger = LoggerFactory.getLogger(CheckOutController.class);

    private UserShippingAddress shippingAddress = new UserShippingAddress();
    private UserBillingAddress billingAddress = new UserBillingAddress();
    private Payment payment = new Payment();

    private JavaMailSender javaMailSender;

    @Autowired
    private ResetMailConstruct mailConstruct;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AddressAndPaymentService addressAndPaymentService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private UserOrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("checkoutCartItems")
    public String checkoutCartItems(@RequestParam("shoppingCartId") Long shoppingCartId, @RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/checkout/checkoutCartItems");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        if (shoppingCartId != user.getShoppingCart().getShoppingCartId()) {
            return "bookstoreportal/badRequest";
        }
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(user.getShoppingCart());
        if (cartItemsList.size() == 0) {
            model.addAttribute("emptyCart", true);
            return "forward:/web/bookStore/shoppingCart/shoppingCart";
        }
        for (CartItems cartItems : cartItemsList) {
            if (cartItems.getBook().getBookInStockNumber() < cartItems.getItemQuantity()) {
                model.addAttribute("notEnoughStock", true);
                return "forward:/web/bookStore/shoppingCart/shoppingCart";
            }
        }
        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();
        model.addAttribute("user", user);
        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);
        if (userPaymentList.size() == 0) {
            model.addAttribute("emptyPaymentList", true);
        } else {
            model.addAttribute("emptyPaymentList", false);
        }
        if (userShippingList.size() == 0) {
            model.addAttribute("emptyShippingList", true);
        } else {
            model.addAttribute("emptyShippingList", false);
        }
        ShoppingCart shoppingCart = user.getShoppingCart();
        for (UserShipping userShipping : userShippingList) {
            if (userShipping.isUserShippingDefault()) {
                addressAndPaymentService.setByUserShipping(userShipping, shippingAddress);
            }
        }
        for (UserPayment userPayment : userPaymentList) {
            if (userPayment.isDefaultPayment()) {
                addressAndPaymentService.setByUserPayment(userPayment, payment);
                addressAndPaymentService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
            }
        }
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemsList);
        model.addAttribute("shoppingCart", user.getShoppingCart());
        List<String> stateList = IndiaConstants.listOfIndiaStateCodes;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("classActiveShipping", true);
        if (missingRequiredField) {
            model.addAttribute("missingRequiredField", true);
        }
        return "bookstoreportal/checkout";
    }

    @PostMapping("checkoutCartItems")
    public String checkoutItemsSave(@ModelAttribute("shippingAddress") UserShippingAddress shippingAddress, @ModelAttribute("payment") Payment payment, @ModelAttribute("billingAddress") UserBillingAddress billingAddress, @ModelAttribute("billingSameAsShipping") String billingSameAsShipping, @ModelAttribute("orderShippingMethod") String orderShippingMethod, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/checkout/checkoutCartItems");
        ShoppingCart shoppingCart = userService.findUserByUserName(authenticatedUser.getUsername()).getShoppingCart();
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(shoppingCart);
        model.addAttribute("cartItemList", cartItemsList);
        if (billingSameAsShipping.equals("true")) {
            billingAddress.setUserBillingAddressName(shippingAddress.getUserShippingAddressName());
            billingAddress.setUserBillingAddressStreet1(shippingAddress.getUserShippingAddressStreet1());
            billingAddress.setUserBillingAddressStreet2(shippingAddress.getUserShippingAddressStreet2());
            billingAddress.setUserBillingAddressCity(shippingAddress.getUserShippingAddressCity());
            billingAddress.setUserBillingAddressState(shippingAddress.getUserShippingAddressState());
            billingAddress.setUserBillingAddressCountry(shippingAddress.getUserShippingAddressCountry());
            billingAddress.setUserBillingAddressZipCode(shippingAddress.getUserShippingAddressZipCode());
        }
        if (shippingAddress.getUserShippingAddressName().isEmpty() || shippingAddress.getUserShippingAddressStreet1().isEmpty() || shippingAddress.getUserShippingAddressStreet2().isEmpty() || shippingAddress.getUserShippingAddressCity().isEmpty() || shippingAddress.getUserShippingAddressState().isEmpty() || shippingAddress.getUserShippingAddressCountry().isEmpty() || shippingAddress.getUserShippingAddressZipCode().isEmpty() || payment.getCardNumber().isEmpty() || payment.getCardCvc() == 0 || billingAddress.getUserBillingAddressName().isEmpty() || billingAddress.getUserBillingAddressStreet1().isEmpty() || billingAddress.getUserBillingAddressStreet2().isEmpty() || billingAddress.getUserBillingAddressCity().isEmpty() || billingAddress.getUserBillingAddressState().isEmpty() || billingAddress.getUserBillingAddressCountry().isEmpty() || billingAddress.getUserBillingAddressZipCode().isEmpty()) {
            return "redirect:/web/bookStore/checkout/checkoutCartItems?shoppingCartId=" + shoppingCart.getShoppingCartId() + "&missingRequiredField=true";
        }
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserOrder order = orderService.createNewUserOrder(shoppingCart, shippingAddress, billingAddress, payment, orderShippingMethod, user);
        javaMailSender.send(mailConstruct.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
        shoppingCartService.clearShoppingCart(shoppingCart);
        LocalDate today = LocalDate.now();
        LocalDate estimatedDeliveryDate;
        if (orderShippingMethod.equals("groundShipping")) {
            estimatedDeliveryDate = today.plusDays(5);
        } else {
            estimatedDeliveryDate = today.plusDays(3);
        }
        model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
        return "bookstoreportal/orderSubmittedPage";
    }

    @PostMapping("setShippingAddress")
    public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/checkout/setShippingAddress");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserShipping userShipping = userShippingService.findByUserShippingId(userShippingId);
        if (userShipping.getUser().getUserId() != user.getUserId()) {
            return "bookstoreportal/badRequest";
        } else {
            addressAndPaymentService.setByUserShipping(userShipping, shippingAddress);
        }
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(user.getShoppingCart());
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemsList);
        model.addAttribute("shoppingCart", user.getShoppingCart());
        List<String> stateList = IndiaConstants.listOfIndiaStateCodes;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();
        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("classActiveShipping", true);
        if (userPaymentList.size() == 0) {
            model.addAttribute("emptyPaymentList", true);
        } else {
            model.addAttribute("emptyPaymentList", false);
        }
        model.addAttribute("emptyShippingList", false);
        return "bookstoreportal/checkout";
    }

    @PostMapping("setPaymentMethod")
    public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/checkout/setPaymentMethod");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserPayment userPayment = userPaymentService.findById(userPaymentId);
        UserBilling userBilling = userPayment.getUserBilling();
        if (userPayment.getUser().getUserId() != user.getUserId()) {
            return "bookstoreportal/badRequest";
        } else {
            addressAndPaymentService.setByUserPayment(userPayment, payment);
        }
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(user.getShoppingCart());
        addressAndPaymentService.setByUserBilling(userBilling, billingAddress);
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemsList);
        model.addAttribute("shoppingCart", user.getShoppingCart());
        List<String> stateList = IndiaConstants.listOfIndiaStateCodes;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();
        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("classActivePayment", true);
        model.addAttribute("emptyPaymentList", false);
        if (userShippingList.size() == 0) {
            model.addAttribute("emptyShippingList", true);
        } else {
            model.addAttribute("emptyShippingList", false);
        }
        return "bookstoreportal/checkout";
    }
}
