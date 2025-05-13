package com.practice.spring_security.spring_security.filter;

import com.practice.spring_security.spring_security.service.JWTService;
import com.practice.spring_security.spring_security.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        Get the Bearer Token from the request header
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            String token = bearerToken.substring(7);
//            Get the username from the token
            String username = jwtService.extractUsername(token);
//            Check if username is not empty and if the user is not already logged in
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

//                Check if the token is valid and not expired
                if(jwtService.validateToken(token,userDetails)){
//                    Create an Authentication Token and set in SecurityContextHolder
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        }

        filterChain.doFilter(request,response);
    }

}
