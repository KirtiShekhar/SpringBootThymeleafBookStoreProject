package com.springboot.thymeleaf.bookstore.project.repository;

import com.springboot.thymeleaf.bookstore.project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBookCategory(String bookCategory);

    List<Book> findByBookTitleContaining(String bookTitle);

    @Query("Select book from Book book where book.bookId=:bookId")
    Book findByBookId(@Param("bookId") Long bookId);
}