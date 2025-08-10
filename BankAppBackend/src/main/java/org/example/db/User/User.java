package org.example.db.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_seq", sequenceName = "users_sequence")
    private Long id;
    @Getter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    private String email;
    @Getter
    private String role;
    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userinfo_id")
    private UserInfo userInfo;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        role = "user";
        UserInfo userInfo = new UserInfo();
        this.userInfo = userInfo;

        log.debug("User {} created", username);
    }

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        UserInfo userInfo = new UserInfo();
        this.userInfo = userInfo;

        log.debug("User {} created", username);
    }


}
