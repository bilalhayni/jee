package com.example.labomasi.config;

import com.example.labomasi.security.CustomAuthenticationSuccessHandler;
import com.example.labomasi.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF protection - enabled for web, disabled for API
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                // Security headers
                .headers(headers -> headers
                        .xssProtection(xss -> xss
                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        )
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com https://cdn.jsdelivr.net; style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://fonts.googleapis.com; font-src 'self' https://cdnjs.cloudflare.com https://fonts.gstatic.com; img-src 'self' data:;")
                        )
                        .frameOptions(frame -> frame.sameOrigin())
                )
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public resources
                        .requestMatchers("/static/**", "/images/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/login", "/register", "/error", "/access-denied").permitAll()

                        // API endpoints require authentication
                        .requestMatchers("/api/**").authenticated()

                        // Admin-only endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/members/new", "/members/*/edit", "/members/*/delete").hasRole("ADMIN")
                        .requestMatchers("/resources/new", "/resources/*/edit", "/resources/*/delete").hasRole("ADMIN")

                        // Admin or Director can manage projects
                        .requestMatchers("/projects/new", "/projects/*/edit").hasAnyRole("ADMIN", "DIRECTOR")
                        .requestMatchers("/projects/*/delete").hasRole("ADMIN")

                        // Teachers, Directors, and Admins can create/edit publications
                        .requestMatchers("/publications/new", "/publications/*/edit").hasAnyRole("ADMIN", "DIRECTOR", "TEACHER")
                        .requestMatchers("/publications/*/delete").hasRole("ADMIN")

                        // All authenticated users can view
                        .requestMatchers("/dashboard", "/", "/projects", "/projects/*", "/publications", "/publications/*",
                                        "/members", "/members/*", "/resources", "/resources/*").authenticated()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                // Form login configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureUrl("/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // Session management
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true")
                )
                // Exception handling
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
