package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> findAllBooks();

    Book findOneBook(Long bookId);

    List<Book> findByCategory(String bookCategory);

    List<Book> blurrySearch(String bookTitle);
}
