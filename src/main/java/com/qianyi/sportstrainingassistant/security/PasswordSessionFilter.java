package com.qianyi.sportstrainingassistant.security;

import com.qianyi.sportstrainingassistant.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Reject old authenticated sessions on their next request after a password change.
// Anonymous registration / email-verification sessions are left untouched.
public class PasswordSessionFilter extends OncePerRequestFilter {

    private final UserRepository users;

    public PasswordSessionFilter(UserRepository users) {
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            boolean valid = authentication.getPrincipal() instanceof AccountPrincipal principal
                    && users.findByUsername(authentication.getName())
                    .map(user -> user.isEmailVerified()
                            && principal.matchesCredentialStamp(user.getPasswordHash()))
                    .orElse(false);
            if (!valid) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                response.setHeader("Cache-Control", "no-store");
                if (request.getServletPath().startsWith("/api/")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\":\"Session expired. Please sign in again.\"}");
                } else {
                    response.sendRedirect(request.getContextPath() + "/login.html?passwordChanged=1");
                }
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
