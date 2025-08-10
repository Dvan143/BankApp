package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.db.News.News;
import org.example.db.News.NewsService;
import org.example.db.User.User;
import org.example.db.User.UserInfo;
import org.example.db.User.UserService;
import org.example.listeners.MetaInfoDto;
import org.example.listeners.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
public class ApiController extends ParentController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    RabbitProducer rabbitProducer;
    @Value("${projectName}")
    String projectName;

    @PostMapping("/changeName")
    public void changeName(@RequestParam(name = "name") String name, Principal principal, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        redirectIfUserUnauthorized(resp);
        try{
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            userService.changeName(user, name);
            log.debug("User {} requested to change name to {}",user.getUsername(), name);
            resp.sendRedirect("/profile");
        } catch (NullPointerException ex) {
            log.debug("Anonymous User with ip: {} tried to change name", req.getRemoteAddr());
            resp.sendRedirect("/error");
        }
    }

    @PostMapping("/changeBirthday")
    public void changeBirthday(@RequestParam(name = "birthday") String birthday, Principal principal, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        redirectIfUserUnauthorized(resp);
        try {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            userService.changeBirthday(user, birthday);
            log.debug("User {} requested to change birthday date to {}",user.getUsername(), birthday);
            resp.sendRedirect("/profile");
        } catch (NullPointerException ex) {
            log.debug("Anonymous User with ip: {} tried to change birthday", req.getRemoteAddr());
            resp.sendRedirect("/error");
        }
    }

    @GetMapping("/getUserInfo")
    public UserInfo getUserInfo(Principal principal, HttpServletResponse resp) throws IOException {
        redirectIfUserUnauthorized(resp);
        try{
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            return userService.getUserInfo(user);
        } catch (NullPointerException ex) {
            resp.sendRedirect("/error");
        }
        return null;
    }

    @PostMapping("/changeEmail")
    public Map<String, String> changeEmail(@RequestBody Map<String, String> data, Principal principal, HttpServletResponse resp) throws IOException {
        redirectIfUserUnauthorized(resp);
        try{
            String username = principal.getName();
            User user = userService.getUserByUsername(username);

            String email = data.get("email");
            if(userService.isEmailRegistered(email)) return Map.of("status", "exist");
            String usernameSender = principal.getName();
            MetaInfoDto metaInfoDto = new MetaInfoDto(projectName, usernameSender, email);
            String generatedCode = generateSecretCode();
            rabbitProducer.sendVerificationCode(metaInfoDto, generatedCode);
            userService.setVerificationCode(user, generatedCode);
            log.debug("User {} requested to change email date to {}",user.getUsername(), email);
//            resp.sendRedirect("/profile");
            return Map.of("status", "ok");
        } catch (NullPointerException ex) {
            log.error(ex.getMessage());
//            resp.sendRedirect("/error");
        }
        return Map.of("status", "error");
    }

    @PostMapping("/checkVerificationCode")
    public Map<String, String> checkVerificationCode(@RequestBody Map<String, String> data, Principal principal, HttpServletResponse resp) throws IOException {
        redirectIfUserUnauthorized(resp);
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        String email = data.get("email");
        String enteredCode = data.get("verificationCode");
        if(userService.checkVerificationCode(user, enteredCode)) {
            userService.changeEmail(user, email);
            return Map.of("status","ok");
        } else {
            return Map.of("status","wrong");
        }
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

    private String generateSecretCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(100000, 999999));
    }
}
