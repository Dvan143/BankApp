package org.example.db.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnotherUserDto {
    private String username;
    private String name;

    public AnotherUserDto() {
    }

    public AnotherUserDto(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
