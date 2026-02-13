package com.chatapp.service;

import com.chatapp.entity.PasswordResetOtp;
import com.chatapp.entity.User;
import com.chatapp.exception.CustomExceptions.InvalidOtpException;
import com.chatapp.exception.CustomExceptions.OtpExpiredException;
import com.chatapp.exception.CustomExceptions.TooManyOtpRequestsException;
import com.chatapp.repository.OtpRepository;
import com.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;



@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final int OTP_LENGTH = 6;
    private static final int MAX_OTP_REQUESTS_PER_HOUR = 3;
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public void generateAndSendOtp(String email) {
        // Verify user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Rate limiting: Check OTP requests in last hour
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        Long recentRequests = otpRepository.countRecentOtpRequests(email, oneHourAgo);

        if (recentRequests >= MAX_OTP_REQUESTS_PER_HOUR) {
            throw new TooManyOtpRequestsException(
                    "Too many OTP requests. Please try again later."
            );
        }

        // Generate secure OTP
        String otp = generateSecureOtp();

        // Create OTP entity
        PasswordResetOtp otpEntity = new PasswordResetOtp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(
                PasswordResetOtp.OTP_EXPIRY_MINUTES
        ));
        otpEntity.setIsUsed(false);
        otpEntity.setAttemptCount(0);

        otpRepository.save(otpEntity);

        // Send OTP via email
        emailService.sendOtpEmail(email, user.getFullName(), otp);
    }

    private String generateSecureOtp() {
        int otp = 100000 + random.nextInt(900000); // 6-digit number
        return String.valueOf(otp);
    }

    @Transactional
    public void verifyOtp(String email, String otp) {
        PasswordResetOtp otpEntity = otpRepository
                .findByEmailAndOtpAndIsUsedFalse(email, otp)
                .orElseThrow(() -> new InvalidOtpException("Invalid OTP"));

        // Check if expired
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired");
        }

        // Check attempt count
        if (otpEntity.getAttemptCount() >= PasswordResetOtp.MAX_ATTEMPTS) {
            throw new InvalidOtpException("Maximum verification attempts exceeded");
        }

        // Mark as used
        otpEntity.setIsUsed(true);
        otpRepository.save(otpEntity);
    }

    @Transactional
    public void incrementOtpAttempt(String email, String otp) {
        otpRepository.findByEmailAndOtpAndIsUsedFalse(email, otp)
                .ifPresent(otpEntity -> {
                    otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
                    otpRepository.save(otpEntity);
                });
    }

    // Scheduled cleanup job (call this periodically)
    @Transactional
    public void cleanupExpiredOtps() {
        otpRepository.deleteExpiredOtps(LocalDateTime.now());
    }
}