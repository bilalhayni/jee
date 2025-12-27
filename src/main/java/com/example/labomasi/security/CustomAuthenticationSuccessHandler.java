package com.example.labomasi.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Clear any authentication attributes before redirect
        request.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        // All roles redirect to the dashboard - role-specific views are handled by DashboardController
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
