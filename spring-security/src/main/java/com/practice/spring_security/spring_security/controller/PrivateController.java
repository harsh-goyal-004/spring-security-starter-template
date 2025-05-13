package com.practice.spring_security.spring_security.controller;

import com.practice.spring_security.spring_security.entity.User;
import com.practice.spring_security.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }




}
