package org.example.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.User;
import org.example.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public void login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, HttpServletResponse resp) throws IOException {
        try{
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
            authenticationManager.authenticate(authToken);
            resp.sendRedirect("/");
        } catch (AuthenticationException ex) { // TODO wrong-password page
            resp.sendRedirect("/login?wrong-credentials");
        }
    }
    @PostMapping("/test")
    public String test() {
        return "OK";
    }

    @PostConstruct
    public void init(){
        User user1 = new User("admin", passwordEncoder.encode("admin"), "admin");
        userService.saveUser(user1);
        User user2 = new User("bobr", passwordEncoder.encode("1234"));
        userService.saveUser(user2);
    }
}
