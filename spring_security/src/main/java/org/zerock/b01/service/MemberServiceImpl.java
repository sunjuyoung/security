package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.repository.MemberRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {

        boolean existsById = memberRepository.existsById(memberJoinDTO.getMid());
        if(existsById){
            throw new MidExistException();
        }
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePwd(passwordEncoder.encode(memberJoinDTO.getPwd()));
        member.addRole(MemberRole.USER);
        memberRepository.save(member);


    }
}
