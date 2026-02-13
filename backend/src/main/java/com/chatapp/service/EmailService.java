package com.chatapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String userName, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset OTP - Chat Application");

            String emailContent = buildOtpEmailContent(userName, otp);
            helper.setText(emailContent, true); // true = HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    private String buildOtpEmailContent(String userName, String otp) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .otp-box { background-color: #fff; padding: 15px; margin: 20px 0; 
                               text-align: center; font-size: 32px; font-weight: bold; 
                               letter-spacing: 5px; border: 2px dashed #4CAF50; }
                    .footer { padding: 20px; text-align: center; font-size: 12px; color: #666; }
                    .warning { color: #d9534f; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Password Reset Request</h1>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>You have requested to reset your password. Please use the OTP below:</p>
                        <div class="otp-box">%s</div>
                        <p><strong>This OTP is valid for 10 minutes.</strong></p>
                        <p class="warning">⚠️ Do not share this OTP with anyone.</p>
                        <p>If you did not request this, please ignore this email.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Chat Application. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, otp);
    }
}