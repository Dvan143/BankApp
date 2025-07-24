package org.example.db;

public class UserDto {
    private String username;
    private String email;

    public UserDto() {
    }

    public UserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
