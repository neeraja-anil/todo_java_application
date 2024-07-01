package com.takeaway.todo.controllers;

import com.takeaway.todo.repo.User;
import com.takeaway.todo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user){
        try {
            if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("Please add all fields");
            }

            // Register the new user
            User newUser = userService.registerUser(user);
            return ResponseEntity.status(201).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Invalid user data");
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authUser(@RequestBody User user){
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("Please add all fields");
            }
            userService.loginUser(user, httpSession);
            return ResponseEntity.status(200).body("Login Success" );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session){
        try {
            String response = userService.logoutUser(session);
            return ResponseEntity.status(200).body(response);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
