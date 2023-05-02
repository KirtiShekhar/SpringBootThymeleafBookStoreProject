package com.springboot.thymeleaf.bookstore.project.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bookstore_application_password_reset_token")
public class PasswordResetToken {
    public static final int PASSWORD_EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_reset_token_id", nullable = false, updatable = false, columnDefinition = "integer")
    private Long passwordResetTokenId;
    @Column(name = "password_reset_token", nullable = false, updatable = false, columnDefinition = "text")
    private String passwordResetToken;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Column(name = "password_reset_token_expiry_date", nullable = false, updatable = false)
    private Date tokenExpiryDate;

    public PasswordResetToken(final String passwordResetToken, final User user) {
        super();
        this.passwordResetToken = passwordResetToken;
        this.user = user;
        this.tokenExpiryDate = calculateExpiryDate(PASSWORD_EXPIRATION);
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }
}
