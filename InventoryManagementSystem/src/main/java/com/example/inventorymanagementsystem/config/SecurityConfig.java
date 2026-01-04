package com.example.inventorymanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.inventorymanagementsystem.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());

        return builder.build();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth

                // 🔓 Public (no login)
                .requestMatchers(
                    "/login.html",
                    "/css/**",
                    "/js/**"
                ).permitAll()

                // 🔓 Orders API (Postman users)
                .requestMatchers("/orders/**").permitAll()

                // 🔐 Admin-only APIs
                .requestMatchers(
                    "/products/**",
                    "/stocks/**"
                ).hasRole("ADMIN")

                // 🔐 Dashboard pages
                .anyRequest().hasRole("ADMIN")
            )

            // 🔐 Login config
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index.html", true)
                .permitAll()
            )

            // ❌ Return 401 instead of login HTML for APIs
            .exceptionHandling(e -> e
                .authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                )
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login.html")
            );

        return http.build();
    }

    
}
