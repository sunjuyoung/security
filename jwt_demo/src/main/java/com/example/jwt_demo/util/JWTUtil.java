package com.example.jwt_demo.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JWTUtil {

    //JWT 쉽게 이용하기 위한 클래스
    //JWT문자열을 생성하는 기능과, 검증하는 기능

    @Value("${jwt.secret.key}")
    private String key;

    public String generateToken(Map<String, Object> valueMap,int days){

        log.info(" key  :" + key);

        //헤더
        Map<String,Object> headers = new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");

        //payload
        Map<String,Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        //유효기간
        int time = (60*24)*days;

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256,key.getBytes())
                .compact();

        return jwtStr;
    }

    public Map<String,Object> validateToken(String token) throws JwtException{

        Map<String,Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claim;
    }


}
