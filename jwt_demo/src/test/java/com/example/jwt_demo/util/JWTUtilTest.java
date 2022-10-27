package com.example.jwt_demo.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class JWTUtilTest {

    //jwtutil 문자열생성
    //jwt.io 검사
    //validateToken() 일치 확인

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void test(){
        Map<String,Object> claimMap = Map.of("mid","user1");
        String jwtStr = jwtUtil.generateToken(claimMap,1);

        log.info(jwtStr);
    }
    @Test
    public void testValidate(){

        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjY5NDY0NDAsIm1pZCI6InVzZXIxIiwiaWF0IjoxNjY2ODYwMDQwfQ.FOYqeVS1ctcs-wDn3SfXQ29yJIsHk2MlI6fzwcCm-xI";

        Map<String, Object> stringObjectMap = jwtUtil.validateToken(jwtStr);
        log.info(stringObjectMap);
    }

}