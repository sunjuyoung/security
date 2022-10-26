package org.zerock.b01.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity{

    @Id
    private String mid;

    private String pwd;
    private String email;
    private boolean del;

    private boolean social;


    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "member_roles",
            joinColumns = @JoinColumn(name = "member_mid"),
            foreignKey = @ForeignKey(
                    name = "member_fk",
                    foreignKeyDefinition = "FOREIGN KEY (member_mid) REFERENCES member(mid) ON DELETE CASCADE")
    )
    private Set<MemberRole> roleSet = new HashSet<>();


    public void changePwd(String pwd){
        this.pwd = pwd;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRole(){
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){
        this.social = social;
    }

}
