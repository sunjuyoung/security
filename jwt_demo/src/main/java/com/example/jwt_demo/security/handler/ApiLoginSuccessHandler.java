package com.example.jwt_demo.security.handler;

import com.example.jwt_demo.util.JWTUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class ApiLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    //Access Token, Refresh Token
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("----- ApiLoginSuccessHandler -----  ");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.info(authentication.getName());
        log.info(authentication);

        Map<String,Object> claim = Map.of("mid",authentication.getName());
        String accessToken = jwtUtil.generateToken(claim,1);
        String refreshToken = jwtUtil.generateToken(claim,10);

        Gson gson = new Gson();

        Map<String,String> keyMap = Map.of("accessToken",accessToken ,
                "refreshToken",refreshToken);
        String jsonStr = gson.toJson(keyMap);
        response.getWriter().println(jsonStr);


    }
}
