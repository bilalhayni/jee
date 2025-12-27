package com.example.labomasi.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/dashboard";

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            switch (role) {
                case "ROLE_ADMIN":
                    redirectUrl = "/dashboard";
                    break;
                case "ROLE_DIRECTOR":
                    redirectUrl = "/dashboard";
                    break;
                case "ROLE_TEACHER":
                    redirectUrl = "/dashboard";
                    break;
                case "ROLE_DOCTORANT":
                    redirectUrl = "/dashboard";
                    break;
                default:
                    redirectUrl = "/dashboard";
            }
        }

        // Clear any authentication attributes before redirect
        request.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
