package com.barbershop.controller;

import com.barbershop.dto.LoginRequestDTO;
import com.barbershop.model.AppUser;
import com.barbershop.repository.AppUserRepository;
import com.barbershop.service.AuthenticationService;
import com.barbershop.security.UserAuthenticated;
import com.barbershop.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        AppUser user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // creates Authentication objects manually
        var authentication = new UsernamePasswordAuthenticationToken(
                new UserAuthenticated(user), null, List.of(() -> "read")
        );

        String token = authenticationService.authenticate(authentication);
        return ResponseEntity.ok(Map.of("accessToken", token));

    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        passwordResetService.requestPasswordReset(email);
        return ResponseEntity.ok(Map.of("message", "If the email exists, a reset link was sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password has been reset successfully"));
    }
}
