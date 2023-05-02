package com.springboot.thymeleaf.bookstore.project.controller;

import com.springboot.thymeleaf.bookstore.project.entity.Book;
import com.springboot.thymeleaf.bookstore.project.entity.User;
import com.springboot.thymeleaf.bookstore.project.service.BookService;
import com.springboot.thymeleaf.bookstore.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/web/bookStore/search")
public class SearchController {
    private Logger bookStoreLogger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("byCategory")
    public String searchByCategory(@RequestParam("bookCategory") String bookCategory, Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/search/byCategory");
        if (authenticatedUser != null) {
            String userName = authenticatedUser.getUsername();
            User user = userService.findUserByUserName(userName);
            model.addAttribute("user", user);
        }
        String classActiveCategory = "active" + bookCategory;
        classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
        classActiveCategory = classActiveCategory.replaceAll("&", "");
        model.addAttribute(classActiveCategory, true);
        List<Book> bookList = bookService.findByCategory(bookCategory);
        if (bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
            return "bookstoreportal/bookShelf";
        } else {
            model.addAttribute("bookList", bookList);
            return "bookstoreportal/bookShelf";
        }
    }

    @GetMapping("byKeyword")
    public String searchByKeyword(@ModelAttribute("keyword") String keyword, Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/search/byKeyword");
        if (authenticatedUser != null) {
            String userName = authenticatedUser.getUsername();
            User user = userService.findUserByUserName(userName);
            model.addAttribute("user", user);
        }
        List<Book> bookList = bookService.blurrySearch(keyword);
        if (bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
            return "bookstoreportal/bookShelf";
        } else {
            model.addAttribute("bookList", bookList);
            return "bookstoreportal/bookShelf";
        }
    }
}
