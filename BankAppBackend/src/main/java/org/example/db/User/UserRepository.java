package org.example.db.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    @Query("SELECT new org.example.db.User.UserDto(u.username, u.email) FROM User u WHERE u.username = ?1 or u.email = ?2")
    List<UserDto> getByUsernameOrEmail(String username, String email);
}
