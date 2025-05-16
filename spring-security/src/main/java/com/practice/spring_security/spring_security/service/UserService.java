package com.practice.spring_security.spring_security.service;

import com.practice.spring_security.spring_security.entity.User;
import com.practice.spring_security.spring_security.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JWTService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

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


//    This method verify the user credentials and generate access and refresh token
    public String verifyUser(User user, HttpServletResponse response) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            if(authentication.isAuthenticated()){
                String accessToken = jwtService.generateToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

//                Store refreshToken in httpOnly cookie
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setSecure(true); //HTTPS ONLY
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 Days
                response.addCookie(cookie);

//                Send the access token in response body
                return accessToken;
            }else{
                return "Error while verifying the User";
            }
        }catch(Exception e){
            return "Invalid username or password";
        }
    }

//    This method validates if a refresh token is still valid and generate a new access token
    public ResponseEntity<String> generateNewAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            if(userDetails != null && jwtService.validateToken(refreshToken, userDetails)) {
                    User user = userRepository.findByUsername(username);
                    String accessToken = jwtService.generateToken(user);
                    return new ResponseEntity<>(accessToken, HttpStatus.OK);
                }

        }
        log.info("The refresh is token is expired or invalid");
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }

}
