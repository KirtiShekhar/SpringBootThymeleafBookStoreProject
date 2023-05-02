package com.springboot.thymeleaf.bookstore.project.controller;

import com.springboot.thymeleaf.bookstore.project.entity.Book;
import com.springboot.thymeleaf.bookstore.project.service.AdminBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Controller
@RequestMapping("/web/admin/book")
public class AdminBookController {
    private Logger bookStoreLogger = LoggerFactory.getLogger(AdminBookController.class);

    @Autowired
    private AdminBookService adminBookService;

    @GetMapping("viewAddBook")
    public String viewAddBook(Model bookModal) {
        bookStoreLogger.info("/web/admin/book/viewAddBook");
        Book book = new Book();
        bookModal.addAttribute("book", book);
        return "adminportal/addBook";
    }

    @PostMapping("addBook")
    public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
        bookStoreLogger.info("/web/admin/book/addBook");
        adminBookService.saveNewBook(book);
        MultipartFile bookImage = book.getBookImage();
        try {
            byte[] bytes = bookImage.getBytes();
            String imageNameExtension = book.getBookId() + ".png";
            BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(new File("D:/IntellijJavaProjects/SpringBootECommereceProjects/SpringBootThymeleafBookStoreProject/src/main/resources/static/image/book/" + imageNameExtension)));
            imageOutputStream.write(bytes);
            imageOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:redirect:/web/admin/book/bookList";
    }

    @GetMapping("bookInfo")
    public String bookInfo(@RequestParam("bookId") Long bookId, Model bookModel) {
        bookStoreLogger.info("/web/admin/book/bookInfo");
        Book book = adminBookService.findOneBook(bookId);
        bookModel.addAttribute("book", book);
        return "adminportal/bookInfo";
    }

    @GetMapping("updateBook")
    public String updateBook(@RequestParam("bookId") Long bookId, Model bookModel) {
        bookStoreLogger.info("/web/admin/book/updateBook");
        Book book = adminBookService.findOneBook(bookId);
        bookModel.addAttribute("book", book);
        return "adminportal/updateBook";
    }

    @PostMapping("updateExistingBook")
    public String updateExistingBook(@ModelAttribute("book") Book book, HttpServletRequest request) {
        bookStoreLogger.info("/web/admin/book/updateExistingBook");
        adminBookService.saveNewBook(book);
        MultipartFile bookImage = book.getBookImage();
        if (!bookImage.isEmpty()) {
            try {
                byte[] bytes = bookImage.getBytes();
                String imageNameExtension = book.getBookId() + ".png";
                BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(new File("D:/IntellijJavaProjects/SpringBootECommereceProjects/SpringBootThymeleafBookStoreProject/src/main/resources/static/image/insertedBooks" + imageNameExtension)));
                imageOutputStream.write(bytes);
                imageOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/web/admin/book/bookInfo?bookId=" + book.getBookId();
    }

    @GetMapping("bookList")
    public String bookList(Model bookModel) {
        bookStoreLogger.info("/web/admin/book/bookList");
        List<Book> bookList = adminBookService.findAllBooks();
        bookModel.addAttribute("bookList", bookList);
        return "adminportal/bookList";
    }

    @PostMapping("removeBook")
    public String removeBook(@ModelAttribute("bookId") String bookId, Model bookModel) {
        bookStoreLogger.info("/web/admin/book/removeBook");
        adminBookService.removeOneBook(Long.parseLong(bookId));
        List<Book> bookList = adminBookService.findAllBooks();
        bookModel.addAttribute("bookList", bookList);
        return "redirect:/web/admin/book/bookList";
    }
}
