package org.zerock.b01.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@ToString
public class MemberSecurityDTO extends User {

    private String mid;
    private String pwd;
    private String email;
    private boolean social;
    private boolean del;

    public MemberSecurityDTO(String username, String password,  String email,boolean social,boolean del,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mid = username;
        this.pwd = password;
        this.email = email;
        this.social = social;
        this.del = del;
    }
}
