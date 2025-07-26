package org.example.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.db.News.News;
import org.example.db.News.NewsService;
import org.example.db.User.RegisterDto;
import org.example.db.User.User;
import org.example.db.User.UserDetailsServiceImpl;
import org.example.db.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController extends ParentController{
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    NewsService newsService;

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

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        User user1 = new User("admin", "admin@admin.com", "admin", "admin");
        userService.saveUser(user1);
        User user2 = new User("bobr", "user@gmail.com", "1234");
        userService.saveUser(user2);

        LocalDate date = LocalDate.now();
        News news = new News("TestTitle", "TestContent", date, user1);
        newsService.saveNews(news);
    }
}
