package org.example.controllers;

import org.example.db.User.*;
import org.example.db.User.Exceptions.NotEnoughMoneyException;
import org.example.db.User.Exceptions.NotExistCurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/bankapp/api/transfer")
public class TransferController extends ParentController {
    @Autowired
    UserService userService;

    @PostMapping("/userInfo")
    public AnotherUserDto userinfo(@RequestBody Map<String, String> data) {
        try{
            String recipientUsername = data.get("username");
            User recipientUser = userService.getUserByUsername(recipientUsername);
            String username = recipientUser.getUsername();
            String name = recipientUser.getUserInfo().getName();
            return new AnotherUserDto(username,name);

        } catch (UsernameNotFoundException ex) {
            return new AnotherUserDto();
        }
    }

    @PostMapping("/sendMoney")
    public Map<String, String> sendMoney(@RequestBody Map<String, String> body, Principal principal) {
        String toUsername = body.get("to");
        String money = body.get("money");
        String currency = body.get("currency");

        User from = userService.getUserByUsername(principal.getName());
        User to = userService.getUserByUsername(toUsername);

        try{
            userService.sendMoney(from, to, money, currency);
            return Map.of("status","ok");
        } catch (NotEnoughMoneyException ex) {
            return Map.of("status","notEnoughMoney");
        } catch (NotExistCurrencyException ex) {
            return Map.of("status", "error");
        }
    }
}
