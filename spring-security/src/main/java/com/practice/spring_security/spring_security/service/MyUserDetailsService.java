package com.practice.spring_security.spring_security.service;

import com.practice.spring_security.spring_security.entity.User;
import com.practice.spring_security.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User dbUser = userRepository.findByUsername(username);

        if(dbUser != null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(dbUser.getUsername())
                    .password(dbUser.getPassword())
                    .build();
        }
        throw new UsernameNotFoundException("User Not Found");
    }
}
