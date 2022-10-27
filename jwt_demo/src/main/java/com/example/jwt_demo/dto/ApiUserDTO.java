package com.example.jwt_demo.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ApiUserDTO extends User {

    private String mid;
    private String pwd;

    public ApiUserDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid = username;
        this.pwd = password;
    }
}
