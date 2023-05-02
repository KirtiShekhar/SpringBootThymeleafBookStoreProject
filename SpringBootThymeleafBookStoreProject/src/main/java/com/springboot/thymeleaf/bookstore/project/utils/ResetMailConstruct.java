package com.springboot.thymeleaf.bookstore.project.utils;

import com.springboot.thymeleaf.bookstore.project.entity.User;
import com.springboot.thymeleaf.bookstore.project.entity.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Component
public class ResetMailConstruct {
    @Autowired
    private Environment environment;

    @Autowired
    private TemplateEngine templateEngine;

    public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user, String password) {

        String url = contextPath + "/newUser?token=" + token;
        String message = "\nPlease click on this link to verify your email and edit your personal information.\n " +
                "\n Your Username is: " + user.getUsername() + " \n Your password is: " + password;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getUserEmailAddress());
        email.setSubject("Ksp Amazing Bookstore - New User");
        email.setText(url + message);
        email.setFrom(environment.getProperty("support.email"));
        return email;
    }

    public MimeMessagePreparator constructOrderConfirmationEmail(User user, UserOrder order, Locale locale) {
        Context context = new Context();
        context.setVariable("Order", order);
        context.setVariable("User", user);
        context.setVariable("CartItemList", order.getCartItemsList());
        String text = templateEngine.process("orderConfirmationEmailTemplate", context);

        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setTo(user.getUserEmailAddress());
                email.setSubject("Order Confirmation : " + order.getUserOrderId());
                email.setText(text, true);
                email.setFrom(new InternetAddress("kirtishekhar1997@gmail.com"));
            }
        };
        return messagePreparator;
    }
}
