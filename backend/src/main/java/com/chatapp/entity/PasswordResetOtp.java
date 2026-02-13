package com.chatapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_otp", indexes = {
        @Index(name = "idx_email_otp", columnList = "email, otp"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 6)
    private String otp; // 6-digit OTP

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 0;

    // OTP expires after 10 minutes
    public static final int OTP_EXPIRY_MINUTES = 10;
    public static final int MAX_ATTEMPTS = 3;
}