package com.springboot.thymeleaf.bookstore.project.repository;

import com.springboot.thymeleaf.bookstore.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    User findByUserEmailAddress(String userEmailAddress);

    User findByUserContactNumber(String userContactNumber);

    User findByUserNameAndUserPassword(String userName, String userPassword);
}