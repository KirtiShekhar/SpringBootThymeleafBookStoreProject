package com.springboot.thymeleaf.bookstore.project.repository;

import com.springboot.thymeleaf.bookstore.project.entity.PasswordResetToken;
import com.springboot.thymeleaf.bookstore.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.stream.Stream;


@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByPasswordResetToken(String passwordResetToken);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByTokenExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.tokenExpiryDate <= ?1")
    void deleteAllTokenExpiryDate(Date now);
}