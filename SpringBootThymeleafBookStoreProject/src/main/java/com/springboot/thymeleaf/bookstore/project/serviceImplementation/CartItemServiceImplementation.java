package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.*;
import com.springboot.thymeleaf.bookstore.project.repository.BookToCartItemRepository;
import com.springboot.thymeleaf.bookstore.project.repository.CartItemsRepository;
import com.springboot.thymeleaf.bookstore.project.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemServiceImplementation implements CartItemService {
    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private BookToCartItemRepository bookToCartItemRepository;

    @Override
    public List<CartItems> findByShoppingCart(ShoppingCart shoppingCart) {
        return cartItemsRepository.findByShoppingCart(shoppingCart);
    }

    @Override
    public CartItems updateCartItems(CartItems cartItems) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(cartItems.getBook().getShopPrice())).multiply(new BigDecimal(cartItems.getItemQuantity()));
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        cartItems.setSubtotal(bigDecimal);
        cartItemsRepository.save(cartItems);
        return cartItems;
    }

    @Override
    public CartItems addBookToCartItem(Book book, User user, int itemQuantity) {
        List<CartItems> cartItemsList = findByShoppingCart(user.getShoppingCart());
        for (CartItems cartItems : cartItemsList) {
            if (book.getBookId() == cartItems.getBook().getBookId()) {
                cartItems.setItemQuantity(cartItems.getItemQuantity() + itemQuantity);
                cartItems.setSubtotal(new BigDecimal(String.valueOf(cartItems.getBook().getShopPrice())).multiply(new BigDecimal(itemQuantity)));
                cartItemsRepository.save(cartItems);
                return cartItems;
            }
        }
        CartItems cartItems = new CartItems();
        cartItems.setShoppingCart(user.getShoppingCart());
        cartItems.setBook(book);
        cartItems.setItemQuantity(cartItems.getItemQuantity() + itemQuantity);
        cartItems.setSubtotal(new BigDecimal(String.valueOf(cartItems.getBook().getShopPrice())).multiply(new BigDecimal(itemQuantity)));
        cartItems = cartItemsRepository.save(cartItems);
        BookToCartItem bookToCartItem = new BookToCartItem();
        bookToCartItem.setBook(book);
        bookToCartItem.setCartItem(cartItems);
        bookToCartItemRepository.save(bookToCartItem);
        return cartItems;
    }

    @Override
    public CartItems findByCartItemId(Long cartItemId) {
        return cartItemsRepository.findById(cartItemId).get();
    }

    @Override
    public void removeCartItems(CartItems cartItems) {
        bookToCartItemRepository.deleteByCartItem(cartItems);
        cartItemsRepository.delete(cartItems);
    }

    @Override
    public CartItems saveCartItems(CartItems cartItems) {
        return cartItemsRepository.save(cartItems);
    }

    @Override
    public List<CartItems> findByUserOrder(UserOrder userOrder) {
        return cartItemsRepository.findByOrder(userOrder);
    }
}
