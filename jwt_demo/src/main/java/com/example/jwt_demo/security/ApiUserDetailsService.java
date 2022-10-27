package com.example.jwt_demo.security;

import com.example.jwt_demo.domain.ApiUser;
import com.example.jwt_demo.dto.ApiUserDTO;
import com.example.jwt_demo.repository.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApiUserDetailsService implements UserDetailsService {

    private final ApiUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ApiUser> user = apiUserRepository.findById(username);
        ApiUser apiUser = user.orElseThrow(() -> new UsernameNotFoundException("not found user"));

        ApiUserDTO apiUserDTO = new ApiUserDTO(apiUser.getMid(),apiUser.getPwd(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        return apiUserDTO;
    }
}
