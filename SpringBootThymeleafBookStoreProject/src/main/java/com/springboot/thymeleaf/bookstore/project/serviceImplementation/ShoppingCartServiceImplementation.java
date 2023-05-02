package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.CartItems;
import com.springboot.thymeleaf.bookstore.project.entity.ShoppingCart;
import com.springboot.thymeleaf.bookstore.project.repository.ShoppingCartRepository;
import com.springboot.thymeleaf.bookstore.project.service.CartItemService;
import com.springboot.thymeleaf.bookstore.project.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingCartServiceImplementation implements ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Override
    @Transactional
    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
        BigDecimal cartTotal = new BigDecimal(0);
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(shoppingCart);
        for (CartItems cartItems : cartItemsList) {
            if (cartItems.getBook().getBookInStockNumber() > 0) {
                cartItemService.updateCartItems(cartItems);
                cartTotal = cartTotal.add(cartItems.getSubtotal());
            }
        }
        shoppingCart.setCartGrandTotal(cartTotal);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(shoppingCart);
        for (CartItems cartItems : cartItemsList) {
            cartItems.setShoppingCart(null);
            cartItemService.saveCartItems(cartItems);
        }
        shoppingCart.setCartGrandTotal(new BigDecimal(0));
        shoppingCartRepository.save(shoppingCart);
    }
}
