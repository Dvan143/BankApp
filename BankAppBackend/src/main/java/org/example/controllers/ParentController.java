package org.example.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public abstract class ParentController {
    protected boolean isUserAuthorized() {
        boolean isUserAuthorized;
        isUserAuthorized = SecurityContextHolder.getContext()!=null &&
                SecurityContextHolder.getContext().getAuthentication()!=null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
        return isUserAuthorized;
    }

    protected void redirectIfUserAuthorized(HttpServletResponse resp) throws IOException {
        if(isUserAuthorized()) resp.sendRedirect("/profile");
    }

    protected void redirectIfUserUnauthorized(HttpServletResponse resp) throws IOException {
        if(!isUserAuthorized()) resp.sendRedirect("/login");
    }
}
