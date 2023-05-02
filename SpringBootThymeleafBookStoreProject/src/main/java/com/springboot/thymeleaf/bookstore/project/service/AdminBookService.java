package com.springboot.thymeleaf.bookstore.project.service;

import com.springboot.thymeleaf.bookstore.project.entity.Book;

import java.util.List;

public interface AdminBookService {
    Book saveNewBook(Book book);

    List<Book> findAllBooks();

    Book findOneBook(Long bookId);

    void removeOneBook(Long bookId);
}
