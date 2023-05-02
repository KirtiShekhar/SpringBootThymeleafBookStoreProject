package com.springboot.thymeleaf.bookstore.project.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Random;


public class SecurityUtils {
    public static final String SALT = "salt";
    public static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    @Bean
    public static String randomPassword() {
        StringBuilder passwordSaltBuilder = new StringBuilder();
        Random passwordRandom = new Random();
        while (passwordSaltBuilder.length() < 18) {
            int passwordSaltIndex = (int) (passwordRandom.nextFloat() * SALTCHARS.length());
            passwordSaltBuilder.append(SALTCHARS.charAt(passwordSaltIndex));
        }
        String passwordSaltString = passwordSaltBuilder.toString();
        System.out.println(passwordSaltString);
        return passwordSaltString;
    }
}
