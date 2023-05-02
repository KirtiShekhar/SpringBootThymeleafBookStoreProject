package com.springboot.thymeleaf.bookstore.project.serviceImplementation;

import com.springboot.thymeleaf.bookstore.project.entity.Book;
import com.springboot.thymeleaf.bookstore.project.repository.BookRepository;
import com.springboot.thymeleaf.bookstore.project.service.AdminBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminBookServiceImplementation implements AdminBookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book saveNewBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findOneBook(Long bookId) {
        return bookRepository.findById(bookId).get();
    }

    @Override
    public void removeOneBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}
