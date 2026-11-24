
package com.qianyi.sportstrainingassistant.config;

import com.qianyi.sportstrainingassistant.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // ==================== Password Encoder ====================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ==================== Database Authentication ====================

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository repository
    ) {
        return username -> {
            var user = repository.findByUsername(username)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "Invalid credentials"
                            )
                    );

            return User.withUsername(user.getUsername())
                    .password(user.getPasswordHash())
                    .roles("USER")
                    .build();
        };
    }

    // ==================== Access Control ====================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UserRepository repository
    ) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // Registration does not require login.
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/csrf",
                                "/login.html",
                                "/register.html",
                                "/forgot-password.html",
                                "/reset-password.html",
                                "/api/auth/password-reset/request",
                                "/api/auth/password-reset/confirm",
                                "/error"
                        ).permitAll()

                        // Users can only access their own data.
                        .requestMatchers("/api/users/{userId}/**")
                        .access((authentication, context) -> {

                            String requestedId =
                                    context.getVariables().get("userId");

                            String username =
                                    authentication.get().getName();

                            boolean allowed =
                                    repository.findByUsername(username)
                                            .map(user ->
                                                    user.getId()
                                                            .toString()
                                                            .equals(requestedId)
                                            )
                                            .orElse(false);

                            return new AuthorizationDecision(allowed);
                        })

                        .anyRequest().authenticated()
                )

                // Use our own login page; the POST /login endpoint remains Spring-managed.
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login.html?error")
                        .permitAll()
                )

                .logout(logout -> logout.permitAll())

                // Registration is a public, non-session API.
                // Keep CSRF enabled for authenticated changes.
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/auth/register"
                        )
                );

        return http.build();
    }
}
