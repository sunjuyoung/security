package com.example.jwt_demo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiUser {

    @Id
    private String mid;
    private String pwd;

    public void changePwd(String pwd){
        this.pwd = pwd;
    }


}
