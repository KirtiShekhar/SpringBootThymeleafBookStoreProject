package com.springboot.thymeleaf.bookstore.project.controller;

import com.springboot.thymeleaf.bookstore.project.entity.*;
import com.springboot.thymeleaf.bookstore.project.security.CustomUserDetailsService;
import com.springboot.thymeleaf.bookstore.project.service.*;
import com.springboot.thymeleaf.bookstore.project.utils.IndiaConstants;
import com.springboot.thymeleaf.bookstore.project.utils.ResetMailConstruct;
import com.springboot.thymeleaf.bookstore.project.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/web/bookStore")
public class HomeController {
    private Logger bookStoreLogger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
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
    private UserPaymentService userPaymentService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private UserOrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/")
    public String redirectHomePage() {
        bookStoreLogger.info("/web/bookStore/home");
        return "redirect:/web/bookStore/home";
    }

    @GetMapping("home")
    public String homePage() {
        bookStoreLogger.info("/web/bookStore/home");
        return "bookstoreportal/index";
    }

    @GetMapping("login")
    public String userLogin(Model model) {
        bookStoreLogger.info("/web/bookStore/login");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("classActiveLogin", true);
            return "bookstoreportal/myAccount";
        } else {
            User user = (User) authentication.getPrincipal();
            System.out.println(user.getUsername());
            return "bookstoreportal/myProfile";
        }
    }

    @GetMapping("myAccount")
    public String myAccountPage() {
        bookStoreLogger.info("/web/bookStore/myAccount");
        return "bookstoreportal/myAccount";
    }

    @GetMapping("frequentlyAskedQuestions")
    public String frequentlyAskedQuestionsPage() {
        bookStoreLogger.info("/web/bookStore/frequentlyAskedQuestions");
        return "bookstoreportal/frequentlyAskedQuestions";
    }

    @GetMapping("hours")
    public String hoursPage() {
        bookStoreLogger.info("/web/bookStore/hours");
        return "bookstoreportal/hours";
    }

    @GetMapping("bookshelf")
    public String bookShelf(Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/bookshelf");
        if (authenticatedUser != null) {
            String userName = authenticatedUser.getUsername();
            User user = userService.findUserByUserName(userName);
            model.addAttribute("user", user);
        }
        List<Book> bookList = bookService.findAllBooks();
        model.addAttribute("bookList", bookList);
        model.addAttribute("activeAll", true);
        return "bookstoreportal/bookShelf";
    }

    @RequestMapping("bookDetails")
    public String bookDetails(@RequestParam("bookId") Long bookId, Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/bookDetails");
        if (authenticatedUser != null) {
            String userName = authenticatedUser.getUsername();
            User user = userService.findUserByUserName(userName);
            model.addAttribute("user", user);
        }
        Book book = bookService.findOneBook(bookId);
        model.addAttribute("book", book);
        List<Integer> quantityList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        model.addAttribute("quantityList", quantityList);
        model.addAttribute("quantity", 1);
        return "bookstoreportal/bookDetail";
    }

    @PostMapping("forgetPassword")
    public String forgetPassword(HttpServletRequest request, @ModelAttribute("userEmailAddress") String userEmailAddress, Model model) {
        bookStoreLogger.info("/web/bookStore/forgetPassword");
        model.addAttribute("classActiveForgetPassword", true);
        User user = userService.findByUserEmailAddress(userEmailAddress);
        if (user == null) {
            model.addAttribute("emailNotExist", true);
            return "bookstoreportal/myAccount";
        } else {
            String password = SecurityUtils.randomPassword();
            String encryptedPassword = SecurityUtils.passwordEncoder().encode(password);
            user.setUserPassword(encryptedPassword);
            userService.saveUser(user);
            String resetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, resetToken);
            String bookAppUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            SimpleMailMessage newPasswordEmailMessage = mailConstruct.constructResetTokenEmail(bookAppUrl, request.getLocale(), resetToken, user, password);
            javaMailSender.send(newPasswordEmailMessage);
            model.addAttribute("forgetPasswordEmailSent", true);
            return "bookstoreportal/myAccount";
        }
    }

    @GetMapping("myProfile")
    public String myProfile(Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/myProfile");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("userOrderList", user.getUserOrderList());
        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddress", true);
        List<String> indiaStatesList = IndiaConstants.listOfIndiaStateCodes;
        Collections.sort(indiaStatesList);
        model.addAttribute("stateList", indiaStatesList);
        model.addAttribute("classActiveEdit", true);
        return "bookstoreportal/myProfile";
    }

    @GetMapping("listOfCreditCards")
    public String listOfCreditCards(Model model, @AuthenticationPrincipal User authenticatedUser, HttpServletRequest request) {
        bookStoreLogger.info("/web/bookStore/listOfCreditCards");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("userOrderList", user.getUserOrderList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        return "bookstoreportal/myProfile";
    }

    @GetMapping("listOfShippingAddresses")
    public String listOfShippingAddresses(Model model, @AuthenticationPrincipal User authenticatedUser, HttpServletRequest request) {
        bookStoreLogger.info("/web/bookStore/listOfShippingAddresses");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("userOrderList", user.getUserOrderList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        return "bookstoreportal/myProfile";
    }

    @GetMapping("addNewCreditCard")
    public String addNewCreditCard(Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/addNewCreditCard");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();
        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);
        List<String> indiaStatesList = IndiaConstants.listOfIndiaStateCodes;
        Collections.sort(indiaStatesList);
        model.addAttribute("stateList", indiaStatesList);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("userOrderList", user.getUserOrderList());
        return "bookstoreportal/myProfile";
    }

    @PostMapping("addNewCreditCard")
    public String addNewCreditCardSave(Model model, @AuthenticationPrincipal User authenticatedUser, @ModelAttribute("userPayment") UserPayment userPayment, @ModelAttribute("userBilling") UserBilling userBilling) {
        bookStoreLogger.info("/web/bookStore/addNewCreditCard");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        userService.updateUserBilling(userBilling, userPayment, user);
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("userOrderList", user.getUserOrderList());
        return "bookstoreportal/myProfile";
    }

    @GetMapping("addNewShippingAddress")
    public String addNewShippingAddress(Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/addNewShippingAddress");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);
        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);
        List<String> indiaStatesList = IndiaConstants.listOfIndiaStateCodes;
        Collections.sort(indiaStatesList);
        model.addAttribute("stateList", indiaStatesList);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("userOrderList", user.getUserOrderList());
        return "bookstoreportal/myProfile";
    }

    @PostMapping("addNewShippingAddress")
    public String addNewShippingAddressSave(Model model, @AuthenticationPrincipal User authenticatedUser, @ModelAttribute("userShipping") UserShipping userShipping) {
        bookStoreLogger.info("/web/bookStore/addNewShippingAddress");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        userService.updateUserShipping(userShipping, user);
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("userOrderList", user.getUserOrderList());
        return "bookstoreportal/myProfile";
    }

    @GetMapping("updateCreditCard")
    public String updateCreditCard(@ModelAttribute("userPaymentId") Long userPaymentId, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/updateCreditCard");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserPayment userPayment = userPaymentService.findById(userPaymentId);
        if (user.getUserId() != userPayment.getUser().getUserId()) {
            return "bookstoreportal/badRequest";
        } else {
            model.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("userBilling", userBilling);
            model.addAttribute("userPayment", userPayment);
            List<String> indiaStatesList = IndiaConstants.listOfIndiaStateCodes;
            Collections.sort(indiaStatesList);
            model.addAttribute("stateList", indiaStatesList);
            model.addAttribute("addNewCreditCard", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("userOrderList", user.getUserOrderList());
            return "bookstoreportal/myProfile";
        }
    }

    @GetMapping("updateUserShipping")
    public String updateUserShipping(@ModelAttribute("userShippingAddressId") Long userShippingAddressId, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/updateUserShipping");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserShipping userShipping = userShippingService.findByUserShippingId(userShippingAddressId);
        if (user.getUserId() != userShipping.getUser().getUserId()) {
            return "bookstoreportal/badRequest";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("userShipping", userShipping);
            List<String> indiaStatesList = IndiaConstants.listOfIndiaStateCodes;
            Collections.sort(indiaStatesList);
            model.addAttribute("stateList", indiaStatesList);
            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("userOrderList", user.getUserOrderList());
            return "bookstoreportal/myProfile";
        }
    }

    @PostMapping("setDefaultPayment")
    public String setDefaultPayment(@ModelAttribute("userPaymentId") Long userPaymentId, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/setDefaultPayment");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        userService.setUserDefaultPayment(userPaymentId, user);
        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("userOrderList", user.getUserOrderList());
        return "bookstoreportal/myProfile";
    }

    @PostMapping("setDefaultShippingAddress")
    public String setDefaultShippingAddress(@ModelAttribute("userShippingId") Long userShippingId, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/setDefaultShippingAddress");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        userService.setUserDefaultShipping(userShippingId, user);
        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("userOrderList", user.getUserOrderList());
        return "bookstoreportal/myProfile";
    }

    @DeleteMapping("removeCreditCard")
    public String removeCreditCard(@ModelAttribute("userPaymentId") Long userPaymentId, Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/removeCreditCard");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserPayment userPayment = userPaymentService.findById(userPaymentId);
        if (user.getUserId() != userPayment.getUser().getUserId()) {
            return "bookstoreportal/badRequest";
        } else {
            model.addAttribute("user", user);
            userPaymentService.removeById(userPaymentId);
            model.addAttribute("addNewCreditCard", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("userOrderList", user.getUserOrderList());
            return "bookstoreportal/myProfile";
        }
    }

    @DeleteMapping("removeUserShipping")
    public String removeUserShipping(@ModelAttribute("userShippingId") Long userShippingId, Model model, @AuthenticationPrincipal User authenticatedUser) {
        bookStoreLogger.info("/web/bookStore/removeUserShipping");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserShipping userShipping = userShippingService.findByUserShippingId(userShippingId);
        if (user.getUserId() != userShipping.getUser().getUserId()) {
            return "bookstoreportal/badRequest";
        } else {
            model.addAttribute("user", user);
            userShippingService.removeByUserShippingId(userShippingId);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("userOrderList", user.getUserOrderList());
            return "bookstoreportal/myProfile";
        }
    }

    @GetMapping("orderDetails")
    public String orderDetails(@RequestParam("userOrderId") Long userOrderId, @AuthenticationPrincipal User authenticatedUser, Model model) {
        bookStoreLogger.info("/web/bookStore/orderDetails");
        User user = userService.findUserByUserName(authenticatedUser.getUsername());
        UserOrder order = orderService.findOneOrder(userOrderId);
        if (order.getUser().getUserId() != user.getUserId()) {
            return "bookstoreportal/badRequest";
        } else {
            List<CartItems> cartItemsList = cartItemService.findByUserOrder(order);
            model.addAttribute("cartItemsList", cartItemsList);
            model.addAttribute("user", user);
            model.addAttribute("order", order);
            model.addAttribute("");
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("userOrderList", user.getUserOrderList());
            UserShipping userShipping = new UserShipping();
            model.addAttribute("userShipping", userShipping);
            List<String> indiaStatesList = IndiaConstants.listOfIndiaStateCodes;
            Collections.sort(indiaStatesList);
            model.addAttribute("stateList", indiaStatesList);
            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("listOfShippingAddress", true);
            model.addAttribute("classActiveOrders", true);
            model.addAttribute("displayOrderDetail", true);
            return "bookstoreportal/myProfile";
        }
    }

    @GetMapping("newUser")
    public String newUser(Locale locale, @RequestParam("token") String passwordResetToken, Model model) {
        bookStoreLogger.info("/web/bookStore/newUser");
        PasswordResetToken token = userService.getPasswordResetToken(passwordResetToken);
        if (token == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:bookstoreportal/badRequest";
        }
        User user = token.getUser();
        String userName = user.getUsername();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("user", user);
        model.addAttribute("classActiveEdit", true);
        return "bookstoreportal/myProfile";
    }

    @PostMapping("newUser")
    public String newUserPost(HttpServletRequest request, Model model, @ModelAttribute("email") String userEmailAddress, @ModelAttribute("username") String userName, @ModelAttribute("contactNumber") String userContactNumber, @ModelAttribute("userFirstName") String userFirstName, @ModelAttribute("userLastName") String userLastName) throws Exception {
        bookStoreLogger.info("/web/bookStore/newUser");
        model.addAttribute("classActiveNewAccount", true);
        model.addAttribute("email", userEmailAddress);
        model.addAttribute("username", userName);
        model.addAttribute("contactNumber", userContactNumber);
        model.addAttribute("userFirstName", userFirstName);
        model.addAttribute("userLastName", userLastName);
        if (userService.findUserByUserName(userName) != null) {
            model.addAttribute("usernameExists", true);
            return "bookstoreportal/myAccount";
        }
        if (userService.findByUserEmailAddress(userEmailAddress) != null) {
            model.addAttribute("emailExists", true);
            return "bookstoreportal/myAccount";
        }
        if (userService.findByUserContactNumber(userContactNumber) != null) {
            model.addAttribute("contactNumberExists", true);
            return "bookstoreportal/myAccount";
        }
        User user = new User();
        user.setUserName(userName);
        user.setUserEmailAddress(userEmailAddress);
        user.setUserContactNumber(userContactNumber);
        user.setUserFirstName(userFirstName);
        user.setUserLastName(userLastName);
        String password = SecurityUtils.randomPassword();
        System.out.println("password :" + password);
        String encryptedPassword = SecurityUtils.passwordEncoder().encode(password);
        System.out.println("encryptedPassword :" + encryptedPassword);
        user.setUserPassword(encryptedPassword);
        UserRole userRole = new UserRole();
        userRole.setUserRoleName("ROLE_USER");
        Set<Roles> roles = new HashSet<>();
        roles.add(new Roles(user, userRole));
        userService.createUser(user, roles);
        String token = UUID.randomUUID().toString();
        System.out.println("token :" + token);
        userService.createPasswordResetTokenForUser(user, token);
        String bookAppUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        SimpleMailMessage newPasswordEmailMessage = mailConstruct.constructResetTokenEmail(bookAppUrl, request.getLocale(), token, user, password);
        javaMailSender.send(newPasswordEmailMessage);
        model.addAttribute("emailSent", true);
        model.addAttribute("orderList", user.getUserOrderList());
        return "bookstoreportal/myAccount";
    }

    @PostMapping("updateUserInfo")
    public String updateUserInfo(@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword, Model model) throws Exception {
        bookStoreLogger.info("/web/bookStore/updateUserInfo");
        User currentUser = userService.findByUserId(user.getUserId());
        if (currentUser == null) {
            throw new Exception("User not found");
        }
        /* check email already exist */
        if (userService.findByUserEmailAddress(user.getUserEmailAddress()) != null) {
            if (userService.findByUserEmailAddress(user.getUserEmailAddress()).getUserId() != currentUser.getUserId()) {
                model.addAttribute("emailExists", true);
                return "bookstoreportal/myProfile";
            }
        }
        /* check username already exist */
        if (userService.findUserByUserName(user.getUsername()) != null) {
            if (userService.findUserByUserName(user.getUsername()).getUserId() != currentUser.getUserId()) {
                model.addAttribute("usernameExists", true);
                return "bookstoreportal/myProfile";
            }
        }
        /* update password */
        if (newPassword != null && !newPassword.isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = SecurityUtils.passwordEncoder();
            String storePassword = currentUser.getUserPassword();
            if (passwordEncoder.matches(user.getPassword(), storePassword)) {
                currentUser.setUserPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);
                return "bookstoreportal/myProfile";
            }
        }
        currentUser.setUserFirstName(user.getUserFirstName());
        currentUser.setUserLastName(user.getUserLastName());
        currentUser.setUserName(user.getUsername());
        currentUser.setUserEmailAddress(user.getUserEmailAddress());
        userService.saveUser(currentUser);
        model.addAttribute("updateSuccess", true);
        model.addAttribute("user", currentUser);
        model.addAttribute("classActiveEdit", true);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddress", true);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(currentUser.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("orderList", user.getUserOrderList());
        return "bookstoreportal/myProfile";
    }

}