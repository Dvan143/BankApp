package org.example.controllers;

import org.example.db.News.NewsDto;
import org.example.db.News.NewsService;
import org.example.db.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController extends ParentController{
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;

    @GetMapping("/latest")
    public List<NewsDto> latest() {
        return newsService.getLatestNews();
    }
}
