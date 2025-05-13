package com.practice.spring_security.spring_security.service;

import com.practice.spring_security.spring_security.entity.User;
import com.practice.spring_security.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JWTService jwtService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    Create New User
    public String createNewUser(User newUser){
        if(!newUser.getUsername().isEmpty() && !newUser.getPassword().isEmpty()){
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(newUser);
            return "User Created Successfully";
        }
        return "User is Invalid";
    }


//    Get All Users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public String verifyUser(User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user);
        }else{
            return "Error while verifying the User";
        }
    }
}
