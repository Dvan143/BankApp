package org.example.db.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void saveUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Map<String, String> isUsernameAndEmailExist(String username, String email){
        List<UserDto> results = userRepository.getByUsernameOrEmail(username,email);
        boolean usernameExists = false;
        boolean emailExists = false;

        for (UserDto result : results) {
            if (username.equals(result.getUsername())) {
                usernameExists = true;
            }
            if (email.equals(result.getEmail())) {
                emailExists = true;
            }
        }

        if (usernameExists && emailExists) {
            return Map.of("status", "bothExist");
        } else if (usernameExists) {
            return Map.of("status", "usernameExist");
        } else if (emailExists) {
            return Map.of("status", "emailExist");
        }

        return Map.of("status", "ok");
    }
}
