package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.*;
import com.springboot.thymeleaf.bookstore.project.repository.UserOrderRepository;
import com.springboot.thymeleaf.bookstore.project.service.CartItemService;
import com.springboot.thymeleaf.bookstore.project.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class UserOrderServiceImplementation implements UserOrderService {
    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private CartItemService cartItemService;

    @Override
    public synchronized UserOrder createNewUserOrder(ShoppingCart shoppingCart, UserShippingAddress userShippingAddress, UserBillingAddress userBillingAddress, Payment payment, String orderShippingMethod, User user) {
        UserOrder order = new UserOrder();
        order.setBillingAddress(userBillingAddress);
        order.setUserOrderStatus("Created Success");
        order.setPayment(payment);
        order.setShippingAddress(userShippingAddress);
        order.setOrderShippingMethod(orderShippingMethod);
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(shoppingCart);
        for (CartItems cartItems : cartItemsList) {
            Book book = cartItems.getBook();
            cartItems.setOrder(order);
            book.setBookInStockNumber(book.getBookInStockNumber());
        }
        order.setCartItemsList(cartItemsList);
        order.setUserOrderDate(Calendar.getInstance().getTime());
        order.setUserOrderTotal(shoppingCart.getCartGrandTotal());
        userShippingAddress.setOrder(order);
        userBillingAddress.setOrder(order);
        payment.setOrder(order);
        order.setUser(user);
        order = userOrderRepository.save(order);
        return order;
    }

    @Override
    public UserOrder findOneOrder(Long userOrderId) {
        return userOrderRepository.findById(userOrderId).get();
    }
}
