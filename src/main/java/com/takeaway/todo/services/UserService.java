package com.takeaway.todo.services;

import com.takeaway.todo.dao.UserRepository;
import com.takeaway.todo.repo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user){

        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // Setting password and saving user
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        return userRepository.save(user);
    }
}
