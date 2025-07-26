package org.example.db.News;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.db.User.User;

import java.time.LocalDate;

@Entity
@Getter
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "news_seq", sequenceName = "news_sequence")
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private LocalDate dateOfPublication;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    public News() {
    }

    public News(String title, String content, LocalDate dateOfPublication, User author) {
        this.title = title;
        this.content = content;
        this.dateOfPublication = dateOfPublication;
        this.author = author;
    }

}
