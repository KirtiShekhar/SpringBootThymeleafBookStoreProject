package com.springboot.thymeleaf.bookstore.project.controller;

import com.springboot.thymeleaf.bookstore.project.entity.Book;
import com.springboot.thymeleaf.bookstore.project.entity.CartItems;
import com.springboot.thymeleaf.bookstore.project.entity.ShoppingCart;
import com.springboot.thymeleaf.bookstore.project.entity.User;
import com.springboot.thymeleaf.bookstore.project.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/bookStore/shoppingCart")
public class ShoppingCartController {
    private Logger bookStoreLogger = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserOrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("shoppingCart")
    public String shoppingCart(Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/shoppingCart/shoppingCart");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<CartItems> cartItemsList = cartItemService.findByShoppingCart(shoppingCart);
        shoppingCartService.updateShoppingCart(shoppingCart);
        model.addAttribute("cartItemList", cartItemsList);
        model.addAttribute("shoppingCart", shoppingCart);
        return "bookstoreportal/shoppingCart";
    }

    @PostMapping("addBookItems")
    public String addBookItems(@ModelAttribute("book") Book book, @ModelAttribute("quantity") String itemQuantity, Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/shoppingCart/addBookItems");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        book = bookService.findOneBook(book.getBookId());
        if (Integer.parseInt(itemQuantity) > book.getBookInStockNumber()) {
            model.addAttribute("notEnoughStock", true);
            return "forward:/web/bookStore/bookDetails?bookId=" + book.getBookId();
        }
        CartItems cartItems = cartItemService.addBookToCartItem(book, user, Integer.parseInt(itemQuantity));
        model.addAttribute("addBookSuccess", true);
        return "forward:/web/bookStore/bookDetails?bookId=" + book.getBookId();
    }

    @PostMapping("updateCartItem")
    public String updateShoppingCart(@ModelAttribute("cartItemId") Long cartItemId, @ModelAttribute("itemQuantity") int itemQuantity) {
        bookStoreLogger.info("/web/bookStore/shoppingCart/updateCartItem");
        CartItems cartItems = cartItemService.findByCartItemId(cartItemId);
        cartItems.setItemQuantity(itemQuantity);
        cartItemService.updateCartItems(cartItems);
        return "forward:/web/bookStore/shoppingCart/shoppingCart";
    }

    @DeleteMapping("removeCartItem")
    public String removeCartItem(@RequestParam("cartItemId") Long cartItemId) {
        bookStoreLogger.info("/web/bookStore/shoppingCart/removeCartItem");
        cartItemService.removeCartItems(cartItemService.findByCartItemId(cartItemId));
        return "forward:/web/bookStore/shoppingCart/shoppingCart";
    }
}
