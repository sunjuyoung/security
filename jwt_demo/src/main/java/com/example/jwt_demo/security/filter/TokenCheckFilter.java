package com.example.jwt_demo.security.filter;

import com.example.jwt_demo.security.ApiUserDetailsService;
import com.example.jwt_demo.security.exception.AccessTokenException;
import com.example.jwt_demo.util.JWTUtil;
import com.sun.tools.jconsole.JConsoleContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    //accessToken 이 없는경우 - 토큰이 없다는 메시지 전달
    //accessToken 이 잘못된경우 -
    //accessToken이 오래된 경우
    private final JWTUtil jwtUtil;
    private final ApiUserDetailsService apiUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if(!path.startsWith("/api/")){
            filterChain.doFilter(request,response);
            return;
        }
        log.info("--- token check filter ----");

        try {
            String headerStr = request.getHeader("Authorization");
            if(headerStr == null || headerStr.length() < 8){
                throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
            }

            //Bearer 생략
            String tokenType = headerStr.substring(0,6);
            String tokenStr =  headerStr.substring(7);
            if(tokenType.equalsIgnoreCase("Bearer") == false){
                throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
            }

            Map<String, Object> payload = jwtUtil.validateToken(tokenStr);

            String mid = (String)payload.get("mid");

            UserDetails userDetails = apiUserDetailsService.loadUserByUsername(mid);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request,response);
        }catch (AccessTokenException e){
            e.sendResponseError(response);
        }

        filterChain.doFilter(request,response);

    }



}
