package com.chatapp.repository;

import com.chatapp.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findByEmailAndOtpAndIsUsedFalse(String email, String otp);

    @Query("SELECT o FROM PasswordResetOtp o WHERE o.email = :email AND o.isUsed = false " +
            "ORDER BY o.createdAt DESC")
    Optional<PasswordResetOtp> findLatestOtpByEmail(String email);

    // Delete expired OTPs (cleanup job)
    @Modifying
    @Query("DELETE FROM PasswordResetOtp o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(LocalDateTime now);

    // Count OTP requests in last N minutes
    @Query("SELECT COUNT(o) FROM PasswordResetOtp o WHERE o.email = :email " +
            "AND o.createdAt > :since")
    Long countRecentOtpRequests(String email, LocalDateTime since);
}