package com.springboot.thymeleaf.bookstore.project.repository;

import com.springboot.thymeleaf.bookstore.project.entity.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {
}