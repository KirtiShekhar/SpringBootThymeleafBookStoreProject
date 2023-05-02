package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.Book;
import com.springboot.thymeleaf.bookstore.project.repository.BookRepository;
import com.springboot.thymeleaf.bookstore.project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImplementation implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findAllBooks() {
        List<Book> bookList = (List<Book>) bookRepository.findAll();
        List<Book> activeBookList = new ArrayList<>();
        for (Book book : bookList) {
            if (book.isActive()) {
                activeBookList.add(book);
            }
        }
        return activeBookList;
    }

    @Override
    public Book findOneBook(Long bookId) {
        return bookRepository.findById(bookId).get();
    }

    @Override
    public List<Book> findByCategory(String bookCategory) {
        List<Book> bookList = bookRepository.findByBookCategory(bookCategory);
        List<Book> activeBookList = new ArrayList<>();
        for (Book book : bookList) {
            if (book.isActive()) {
                activeBookList.add(book);
            }
        }
        return activeBookList;
    }

    @Override
    public List<Book> blurrySearch(String bookTitle) {
        List<Book> bookList = bookRepository.findByBookTitleContaining(bookTitle);
        List<Book> activeBookList = new ArrayList<>();
        for (Book book : bookList) {
            if (book.isActive()) {
                activeBookList.add(book);
            }
        }
        return activeBookList;
    }
}
