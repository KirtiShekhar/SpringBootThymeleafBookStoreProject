package com.springboot.thymeleaf.bookstore.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {
    private Logger bookStoreLogger = LoggerFactory.getLogger(AppController.class);

    @RequestMapping("/web/bookStore/home")
    public String bookStoreHomePage() {
        bookStoreLogger.info("/web/bookStore/home");
        return "bookstoreportal/index";
    }

    @RequestMapping("/web/bookStore/error")
    public String bookStoreErrorPage() {
        bookStoreLogger.info("/web/bookStore/error");
        return "bookstoreportal/badRequest";
    }

    @RequestMapping("/web/admin/home")
    public String adminHomePage() {
        bookStoreLogger.info("/web/admin/home");
        return "adminportal/home";
    }
}
