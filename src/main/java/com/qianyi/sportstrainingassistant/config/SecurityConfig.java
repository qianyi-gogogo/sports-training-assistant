
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
import org.springframework.security.authentication.DisabledException;
import java.util.Locale;

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
                    .disabled(!user.isEmailVerified())
                    .roles("USER")
                    .build();
        };
    }

    // ==================== Access Control ====================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UserRepository repository,
            PasswordEncoder passwordEncoder
    ) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // Registration does not require login.
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/csrf",
                                "/api/auth/email/send-code",
                                "/api/auth/email/verify",
                                "/login.html",
                                "/register.html",
                                "/forgot-password.html",
                                "/error",
                                "/api/auth/email/session"
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
// Only users with a verification session can access this page.
                                .requestMatchers("/verify-email.html")
                                .access((authentication, context) -> {

                                    var session = context.getRequest()
                                            .getSession(false);

                                    boolean allowed =
                                            session != null
                                                    && session.getAttribute("verificationUserId")
                                                    instanceof Long;

                                    return new AuthorizationDecision(allowed);
                                })

                        .anyRequest().authenticated()
                )

                // Use our own login page; the POST /login endpoint remains Spring-managed.
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {

                            var session = request.getSession(false);

                            if (session != null) {
                                session.removeAttribute("verificationUserId");
                            }

                            response.sendRedirect("/");
                        })

                        .failureHandler((request, response, exception) -> {

                            // Remove any previous verification permission.
                            var session = request.getSession(false);

                            if (session != null) {
                                session.removeAttribute("verificationUserId");
                            }

                            // Handle accounts that have not completed email verification.
                            if (exception instanceof DisabledException) {

                                String username = request.getParameter("username");
                                String password = request.getParameter("password");

                                if (username != null && password != null) {

                                    var result = repository.findByUsername(
                                            username.trim().toLowerCase(Locale.ROOT)
                                    );

                                    if (result.isPresent()) {

                                        var user = result.get();

                                        if (!user.isEmailVerified()
                                                && user.getEmail() != null
                                                && passwordEncoder.matches(
                                                password,
                                                user.getPasswordHash()
                                        )) {

                                            var verificationSession =
                                                    request.getSession(true);

                                            // Rotate the session ID before granting permission.
                                            request.changeSessionId();

                                            verificationSession.setAttribute(
                                                    "verificationUserId",
                                                    user.getId()
                                            );

                                            response.sendRedirect(
                                                    "/verify-email.html"
                                            );

                                            return;
                                        }
                                    }
                                }
                            }

                            // Use a generic message for unsuccessful authentication.
                            response.sendRedirect("/login.html?error");
                        })

                        .permitAll()
                )

                .logout(logout -> logout.permitAll())

                // Registration is a public, non-session API.
                // Keep CSRF enabled for authenticated changes.
                .csrf(Customizer.withDefaults());

        return http.build();
    }
}
