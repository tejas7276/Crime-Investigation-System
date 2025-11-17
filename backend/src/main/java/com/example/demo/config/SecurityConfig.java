package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // Public Endpoints
                .requestMatchers("/api/users/register", "/api/users/login", "/api/users/updateUser/**").permitAll()
                .requestMatchers("/api/notifications/user/**", "/api/notifications/mark-as-read/**").permitAll()
                .requestMatchers("/api/users/migrate-passwords").permitAll()

                // Complaint Endpoints
                .requestMatchers("/api/complaints", "/api/complaints/**", "/api/complaints/user/**").permitAll()
                .requestMatchers("/api/complaints/status/**").permitAll()
                .requestMatchers("/api/complaints/file/{userId}").permitAll()
                .requestMatchers("/api/complaints/view-all").permitAll() // for testing

                // Officer/Admin Endpoints
                .requestMatchers("/api/officers/add", "/api/admin/**").permitAll()
                .requestMatchers("/api/complaints/officer/**").hasAuthority("OFFICER")
                .requestMatchers("/api/complaints/status/**").hasAuthority("OFFICER")

                // PUT needs auth
                .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()

                // Emergency APIs (using antMatcher)
                .requestMatchers(
                	    antMatcher(HttpMethod.POST, "/api/emergency"),
                	    antMatcher(HttpMethod.PATCH, "/api/emergency/**/respond"), // Changed from /** to /*
                	    antMatcher(HttpMethod.GET, "/api/emergency/active")
                	).permitAll()

                // Everything else needs authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200")); // frontend URL
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
