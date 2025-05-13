package com.practice.spring_security.spring_security.controller;

import com.practice.spring_security.spring_security.entity.User;
import com.practice.spring_security.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String checkHealth(){
        return "Everything fine";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createNewUser(@RequestBody User newUser){
        String response = userService.createNewUser(newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody User user){
        return userService.verifyUser(user);
    }

}
