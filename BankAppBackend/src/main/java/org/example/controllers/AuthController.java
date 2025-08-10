package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.db.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
public class AuthController extends ParentController{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public void login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        // TODO exceptions handling
        redirectIfUserAuthorized(resp);
        try{
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
            authenticationManager.authenticate(authToken);
            log.debug("User {} logging", username);
            resp.sendRedirect("/");
        } catch (AuthenticationException ex) { // TODO wrong-password page
            log.debug("Anonymous User with ip {} entered wrong password", req.getRemoteAddr());
            resp.sendRedirect("/wrong-credentials");
        }
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterDto data, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        redirectIfUserAuthorized(resp);
        Map<String, String> answer = userService.isUsernameAndEmailExist(data.getUsername(), data.getEmail());
        if(answer.get("status").equals("ok")) {
            User newUser = new User(data.getUsername(), data.getEmail(), data.getPassword());
            userService.saveUser(newUser);

            UserDetails userDetails = userDetailsService.loadUserByUsername(data.getUsername());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.debug("User {} is registered.",data.getUsername());
        } else {
            log.debug("Anonymous User with ip {} got error: {}", req.getRemoteAddr(), answer);
        }
        return answer;
    }


}
