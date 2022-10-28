package com.example.jwt_demo.security.filter;


import com.example.jwt_demo.util.JWTUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    /**
     * accessToken 존재여부 확인
     * refreshToken 만료여부 확인
     * 만료기간이 지났다면 재인증을 통해 토큰들을 발급
     * ----------
     * accessToken 만 새로 만들어지는 경우
     * refreshToken도 만료기간이 얼마 남지 않아 모두 새로 만들어야 하는 경우
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if(!path.equals("/refreshToken")){
            filterChain.doFilter(request,response);
            return;
        }

        log.info("---- refresh token filter ------");
        Map<String,String> tokens = parseRequestJson(request);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
       
        jwtUtil.validateToken(accessToken);


        Map<String, Object> refreshValue = jwtUtil.validateToken(refreshToken);
        //refreshToken 유효기간 얼마남지 않은 경우
        Integer exp =(Integer) refreshValue.get("exp");
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        Date currentTime = new Date(System.currentTimeMillis());

        //3일 미만인 경우 다시 생성
        long gapTime = (expTime.getTime() - currentTime.getTime());
        String mid = (String) refreshValue.get("mid");
        accessToken = jwtUtil.generateToken(Map.of("mid",mid),1);
        if(gapTime < (1000 * 60  * 3  ) ){
            refreshToken = jwtUtil.generateToken(Map.of("mid",mid),15);
        }

        log.info("-- refresh token result ...........");
        log.info("-- refresh token : {}",refreshToken);
        log.info("-- access token : {}",accessToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private Map<String, String> parseRequestJson(HttpServletRequest request) throws IOException {
        Reader reader = new InputStreamReader(request.getInputStream());
        Gson gson = new Gson();
        log.info("--- parseJson ---");
        return gson.fromJson(reader, Map.class);
    }
}
