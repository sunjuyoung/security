package org.zerock.b01.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {

    private String email;
    private String pwd;
    private String mid;
    private boolean del;
    private boolean social;

}
