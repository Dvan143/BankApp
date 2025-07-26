package org.example.db.News;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NewsDto {
    private String title;
    private String content;
    private LocalDate dateOfPublication;
    private String authorUsername;

    public NewsDto() {
    }

    public NewsDto(News news) {
        this.title = news.getTitle();
        this.content = news.getContent();
        this.dateOfPublication = news.getDateOfPublication();
        this.authorUsername = news.getAuthor().getUsername();
    }

}
