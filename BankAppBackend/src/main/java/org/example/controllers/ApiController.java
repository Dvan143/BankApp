package org.example.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.example.db.RegisterDto;
import org.example.db.User;
import org.example.db.UserDetailsServiceImpl;
import org.example.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public void login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, HttpServletResponse resp) throws IOException {
        redirectIfUserAuthorized(resp);

        try{
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
            authenticationManager.authenticate(authToken);
            log.info("User {} loggined", username);
            resp.sendRedirect("/");
        } catch (AuthenticationException ex) { // TODO wrong-password page
            log.info("Login error");
            resp.sendRedirect("/wrong-credentials");
        }
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterDto data, HttpServletResponse resp) throws IOException {
        redirectIfUserAuthorized(resp);

        Map<String, String> answer = userService.isUsernameAndEmailExist(data.getUsername(), data.getEmail());
        if(answer.get("status").equals("ok")) {
            User newUser = new User(data.getUsername(), data.getEmail(), data.getPassword());
            userService.saveUser(newUser);

            UserDetails userDetails = userDetailsService.loadUserByUsername(data.getUsername());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.info("User {} is registered.",data.getUsername());
        } else {
            log.info("Register error: {}", answer);
        }
        return answer;
    }

    @PostConstruct
    public void init(){
        User user1 = new User("admin", "admin@admin.com", "admin", "admin");
        userService.saveUser(user1);
        User user2 = new User("bobr", "user@gmail.com", "1234");
        userService.saveUser(user2);
    }

    private boolean isUserAuthorized() {
        boolean isUserAuthorized;
        isUserAuthorized = SecurityContextHolder.getContext()!=null &&
                SecurityContextHolder.getContext().getAuthentication()!=null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
        return isUserAuthorized;
    }

    private void redirectIfUserAuthorized(HttpServletResponse resp) throws IOException {
        if(isUserAuthorized()) resp.sendRedirect("/");
    }

    private void redirectIfUserUnauthorized(HttpServletResponse resp) throws IOException {
        if(!isUserAuthorized()) resp.sendRedirect("/login");
    }
}
