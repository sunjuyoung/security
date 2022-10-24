package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test1(){
        for(int i=1; i<10; i++){
            Member member = Member.builder()
                    .del(false)
                    .mid("member"+i)
                    .email("test@test.com")
                    .pwd(passwordEncoder.encode("1234"))
                    .social(false)
                    .build();
            member.addRole(MemberRole.USER);
            if(i==9){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        }
        Optional<Member> member1 = memberRepository.getWithRoles("member9");
        Member member = member1.get();
        assertEquals(member.getRoleSet().size(),2);
    }



}