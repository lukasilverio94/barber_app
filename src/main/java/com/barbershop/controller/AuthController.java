package com.barbershop.controller;

import com.barbershop.dto.LoginRequestDTO;
import com.barbershop.model.AppUser;
import com.barbershop.repository.AppUserRepository;
import com.barbershop.security.AuthenticationService;
import com.barbershop.security.UserAuthenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
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
    private final JwtEncoder jwtEncoder;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
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
}
