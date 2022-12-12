package com.example.jwt_demo.config;

import com.example.jwt_demo.security.ApiUserDetailsService;
import com.example.jwt_demo.security.filter.APILoginFilter;
import com.example.jwt_demo.security.filter.RefreshTokenFilter;
import com.example.jwt_demo.security.filter.TokenCheckFilter;
import com.example.jwt_demo.security.handler.ApiLoginSuccessHandler;
import com.example.jwt_demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final ApiUserDetailsService apiUserDetailsService;
    private final JWTUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(apiUserDetailsService)
                .passwordEncoder(passwordEncoder());

        // get AuthenticationManager
        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        //apiLoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        //APiLoginSuccessHandler
        ApiLoginSuccessHandler successHandler = new ApiLoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        //apiLoginFilter 위치 조정
        //스프링 시큐리티의 username, password 를 처리하는 UsernamePasswordAuthenticationFilter 의 앞쪽으로 동작하도록 설정
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);


        //api ㅅㅣ작하는 모든 경로는 동작
        TokenCheckFilter tokencheck = new TokenCheckFilter(jwtUtil,apiUserDetailsService);
        http.addFilterBefore(tokencheck,UsernamePasswordAuthenticationFilter.class);

        //refreshToken 호출 자리
        http.addFilterBefore(new RefreshTokenFilter(jwtUtil),TokenCheckFilter.class);


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });


        return http.build();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
