package com.example.jwt_demo.repository;

import com.example.jwt_demo.domain.ApiUser;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class ApiUserRepositoryTest {

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void test(){

        ApiUser apiUser = ApiUser.builder()
                .mid("apiUser1")
                .pwd(passwordEncoder.encode("1234"))
                .build();

        apiUserRepository.save(apiUser);

    }
}