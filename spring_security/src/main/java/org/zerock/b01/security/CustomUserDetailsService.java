package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberSecurityDTO;
import org.zerock.b01.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    //private final PasswordEncoder passwordEncoder;

    //스프링 시큐리티는 내부적으로 UserDetails 타입의 객체를 이용해서 패스워드를 검사하고,
    //사용자 권한을 확인합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserDetails userDetails = User.builder()
//                                    .username(username)
//                                    .password(passwordEncoder.encode("1234"))
//                                    .authorities("ROLE_USER")
//                                    .build();

        Optional<Member> withRoles = memberRepository.getWithRoles(username);
        Member member = withRoles.orElseThrow();

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),member.getPwd(), member.getEmail(),member.isDel(),false,
                member.getRoleSet().stream()
                        .map(a-> new SimpleGrantedAuthority("ROLE_"+a.name())).collect(Collectors.toList())
        );

        log.info(memberSecurityDTO.getMid());
        return memberSecurityDTO;
    }
}
