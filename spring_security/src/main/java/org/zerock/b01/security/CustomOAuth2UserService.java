package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.dto.MemberSecurityDTO;
import org.zerock.b01.repository.MemberRepository;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    final static String KAKAO_PWD = "kakao9";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("NAME : " +clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String,Object> paramMap = oAuth2User.getAttributes();
        String email = null;

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        return generateDTO(email,paramMap);
    }

    private OAuth2User generateDTO(String email, Map<String, Object> paramMap) {
        Optional<Member> result = memberRepository.findByEmail(email);

        //해당 이메일 사용자가 없다면 ,새로운회원으로 저장 oauth2User 구성된 memberSecurityDTO 반환
        //social 값을 true로해서 소셜가입 이메일로 직접 로그인은 불가능하다
        //추후 일반 회원으로 전환 할 수 있는 화면제공
        if(result.isEmpty()){
            Member member = Member.builder()
                    .mid(email)
                    .email(email)
                    .pwd(passwordEncoder.encode(KAKAO_PWD))
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(email,KAKAO_PWD,email,true,false,
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(paramMap);

            return memberSecurityDTO;

        }else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(member.getMid(),
                            member.getPwd(),
                            member.getEmail(),
                            member.isSocial(),
                            member.isDel(),
                            member.getRoleSet().stream()
                                    .map(a->new SimpleGrantedAuthority(a.name())).collect(Collectors.toList()));
            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("===== KAKAO ====");

        Object value = paramMap.get("kakao_account");

        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String) accountMap.get("email");

        return email;
    }
}
