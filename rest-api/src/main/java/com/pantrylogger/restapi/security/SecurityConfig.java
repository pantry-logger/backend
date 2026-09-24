package com.pantrylogger.restapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    // CSRF protection is not applicable: API is stateless and uses JWT bearer tokens
    // exclusively (no cookie-based session/auth), so there is no ambient credential
    // for a cross-site request to exploit. See Spring Security docs on CSRF + REST APIs.
    @SuppressWarnings("java:S4502")
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtToUserDetailsConverter jwtToUserDetailsConverter
    ) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                jwtToUserDetailsConverter)));

        return http.build();
    }
}