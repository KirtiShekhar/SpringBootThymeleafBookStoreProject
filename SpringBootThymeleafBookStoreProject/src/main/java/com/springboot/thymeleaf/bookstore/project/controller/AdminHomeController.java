package com.springboot.thymeleaf.bookstore.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/admin")
public class AdminHomeController {
    private Logger bookStoreLogger = LoggerFactory.getLogger(AdminHomeController.class);

    @GetMapping("/")
    public String redirectHomePage() {
        bookStoreLogger.info("/web/admin/home");
        return "redirect:/web/admin/home";
    }

    @GetMapping("home")
    public String homePage() {
        bookStoreLogger.info("/web/admin/home");
        return "adminportal/home";
    }

    @GetMapping("adminLogin")
    public String adminLoginPage() {
        bookStoreLogger.info("/web/admin/adminLogin");
        return "adminportal/adminLogin";
    }

    @GetMapping("logout")
    public String logoutRedirectHomePage() {
        bookStoreLogger.info("/web/admin/logout");
        return "redirect:/web/admin/home";
    }
}
