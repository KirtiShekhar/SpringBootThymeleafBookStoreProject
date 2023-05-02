package com.springboot.thymeleaf.bookstore.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookstore_application_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false, updatable = false, columnDefinition = "int")
    private Long bookId;
    @Column(name = "book_title", nullable = false, updatable = false, columnDefinition = "text")
    private String bookTitle;
    @Column(name = "book_author", nullable = false, updatable = false, columnDefinition = "text")
    private String bookAuthor;
    @Column(name = "book_description", nullable = false, updatable = false, columnDefinition = "text")
    private String bookDescription;
    @Column(name = "book_publisher", nullable = false, updatable = false, columnDefinition = "text")
    private String bookPublisher;
    @Column(name = "book_publication_date", nullable = false, updatable = false, columnDefinition = "text")
    private String bookPublicationDate;
    @Column(name = "book_language", nullable = false, updatable = false, columnDefinition = "text")
    private String bookLanguage;
    @Column(name = "book_category", nullable = false, updatable = false, columnDefinition = "text")
    private String bookCategory;
    @Column(name = "book_number_of_pages", nullable = false, updatable = false, columnDefinition = "int")
    private int bookNumberOfPages;
    @Column(name = "book_format", nullable = false, updatable = false, columnDefinition = "text")
    private String bookFormat;
    @Column(name = "book_isbn_number", nullable = false, updatable = false, columnDefinition = "text")
    private String bookIsbnNumber;
    @Column(name = "book_shipping_weight", nullable = false, updatable = false, columnDefinition = "int")
    private int shippingWeight;
    @Column(name = "book_list_price", nullable = false, updatable = false, columnDefinition = "int")
    private int listPrice;
    @Column(name = "book_shop_price", nullable = false, updatable = false, columnDefinition = "int")
    private int shopPrice;
    @Column(name = "book_active", nullable = false, updatable = false, columnDefinition = "BIT(1)")
    private boolean active = true;
    @Column(name = "book_in_stock_number", nullable = false, updatable = false, columnDefinition = "int")
    private int bookInStockNumber;
    @Transient
    private MultipartFile bookImage;
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookToCartItem> bookToCartItemList;
}
