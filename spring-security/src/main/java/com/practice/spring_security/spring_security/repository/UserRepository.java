package com.practice.spring_security.spring_security.repository;

import com.practice.spring_security.spring_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);

}
