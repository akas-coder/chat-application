package com.chatapp.controller;

import com.chatapp.dto.OtpRequest;
import com.chatapp.dto.OtpVerifyRequest;
import com.chatapp.dto.PasswordResetRequest;
import com.chatapp.service.OtpService;
import com.chatapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final OtpService otpService;
    private final UserService userService;

    /**
     * Step 1: Request OTP - User enters email
     */
    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@Valid @RequestBody OtpRequest request) {
        try {
            otpService.generateAndSendOtp(request.getEmail());

            return ResponseEntity.ok(Map.of(
                    "message", "OTP sent to your email",
                    "email", request.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * Step 2: Verify OTP - User enters OTP
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        try {
            otpService.verifyOtp(request.getEmail(), request.getOtp());

            return ResponseEntity.ok(Map.of(
                    "message", "OTP verified successfully",
                    "verified", true
            ));
        } catch (Exception e) {
            // Increment attempt count
            otpService.incrementOtpAttempt(request.getEmail(), request.getOtp());

            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage(), "verified", false)
            );
        }
    }

    /**
     * Step 3: Reset Password - User enters new password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        try {
            // Verify OTP again for security
//            otpService.verifyOtp(request.getEmail(), request.getOtp());

            // Reset password
            userService.resetPassword(request.getEmail(), request.getNewPassword());

            return ResponseEntity.ok(Map.of(
                    "message", "Password reset successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}