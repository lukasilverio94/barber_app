package com.barbershop.service;

import com.barbershop.exception.ExpiredResetTokenException;
import com.barbershop.exception.InvalidResetTokenException;
import com.barbershop.model.Customer;
import com.barbershop.model.Email;
import com.barbershop.model.PasswordResetToken;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.repository.PasswordResetTokenRepository;
import com.barbershop.service.util.ResetPasswordEmailBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    private final PasswordResetTokenRepository tokenRepository;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CustomerRepository customerRepository;

    @Value("${frontend.reset-password.url}")
    private String resetPasswordBaseUrl;

    @Transactional
    public void requestPasswordReset(String email) {

        Customer user = customerService.findUserByEmailOrThrow(email);

        String token = UUID.randomUUID().toString();
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(Instant.now().plusSeconds(3600));

        System.out.println("UNDECODED TOKEN= " + token);
        System.out.println("ENCODED TOKEN= " + encodedToken);

        tokenRepository.save(resetToken);

        String resetLink = resetPasswordBaseUrl + "?token=" + encodedToken;
        Email emailTo = ResetPasswordEmailBuilder.buildResetPasswordEmail(user, resetLink);
        emailService.sendSimpleEmailAsync(emailTo);
        log.info("Password reset requested for email: {}", email);
        log.info("Token sent to email: {}", token);

    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidResetTokenException("Reset token is invalid or has already been used"));

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new ExpiredResetTokenException("Token has expired. Please request a new password reset");
        }

        Customer user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        Email successEmail = ResetPasswordEmailBuilder.buildPasswordResetSuccessEmail(user);
        emailService.sendSimpleEmailAsync(successEmail);
        customerRepository.save(user);
        tokenRepository.delete(resetToken);
        log.info("Password reset was successfully completed for email: {}", user.getEmail());

    }

}
