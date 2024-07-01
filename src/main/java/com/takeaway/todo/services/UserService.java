package com.takeaway.todo.services;

import com.takeaway.todo.dao.UserRepository;
import com.takeaway.todo.repo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void loginUser(User user, HttpSession httpSession){
        String email = user.getEmail();
        Optional<User> optionalUser =  userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            // hash user typed password and match it with stored password
            User existingUser = optionalUser.get();
            System.out.println(existingUser);

            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                httpSession.setAttribute("user", existingUser);
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        }else{
            throw new RuntimeException("Invalid Credentials");
        }
    }

    public String logoutUser(HttpSession session){
        if(session.getAttribute("user") != null){
            System.out.println(session.getId());
            session.invalidate();
            return "Logout success";
        }
        else {
            throw new RuntimeException("No logged-in user found");
        }
    }

}
