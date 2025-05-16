package com.practice.spring_security.spring_security.controller;

import com.practice.spring_security.spring_security.entity.User;
import com.practice.spring_security.spring_security.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody  User user){
        String newUser = userService.createNewUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response){
        String s = userService.verifyUser(user, response);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> generateNewAccessToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

//        Get the refresh token from the cookie
        if(cookies != null){
            for(Cookie c : cookies){
                if("refreshToken".equals(c.getName())){
                    String refreshToken = c.getValue();
                    if(!refreshToken.isEmpty()){
                       return userService.generateNewAccessToken(refreshToken);
                    }
                }
            }
        }
        return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
    }

}
