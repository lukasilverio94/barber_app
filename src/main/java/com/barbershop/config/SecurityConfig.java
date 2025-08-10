package com.barbershop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtConfig jwtConfig;

    public SecurityConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        final String BARBERS_API = "/api/barbers";
        final String CUSTOMERS_API = "/api/customers";
        final String APPOINTMENTS_API = "/api/appointments";
        final String BARBER_ROLE = "BARBER";
        final String CUSTOMER_ROLE = "CUSTOMER";

        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(
                        auth -> auth

                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/swagger-ui/index.html").permitAll()
                                // public endpoints
                                .requestMatchers(HttpMethod.POST, CUSTOMERS_API).permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                                // restricted endpoints
                                .requestMatchers(HttpMethod.POST, BARBERS_API).hasRole(BARBER_ROLE)
                                .requestMatchers(HttpMethod.PUT, BARBERS_API).hasRole(BARBER_ROLE)
                                .requestMatchers(HttpMethod.GET, BARBERS_API).hasRole(BARBER_ROLE)
                                .requestMatchers(HttpMethod.GET, CUSTOMERS_API).hasRole(BARBER_ROLE)
                                .requestMatchers(HttpMethod.DELETE, BARBERS_API).hasRole(BARBER_ROLE)
                                .requestMatchers(HttpMethod.PUT, BARBERS_API).hasRole(BARBER_ROLE)
                                .requestMatchers(HttpMethod.PATCH, BARBERS_API).hasRole(BARBER_ROLE)
                                .requestMatchers(HttpMethod.POST, APPOINTMENTS_API).hasRole(CUSTOMER_ROLE)
                                .requestMatchers(HttpMethod.GET, APPOINTMENTS_API).hasRole(BARBER_ROLE)

                                // any other request must be authenticated
                                .anyRequest().authenticated()

                )
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(conf -> conf
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConfig.jwtAuthenticationConverter()))
                );
        return http.build();
    }
}
