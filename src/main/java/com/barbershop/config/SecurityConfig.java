package com.barbershop.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.public.key}")
    private RSAPublicKey key;
    @Value("${jwt.private.key}")
    private RSAPrivateKey priv;

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
                                .requestMatchers("/h2-console/**").permitAll()
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
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authenticationConverter;
    }
}
