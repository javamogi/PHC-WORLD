package com.phcworld.jwt.service;

import com.phcworld.exception.model.NotFoundException;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(Long.valueOf(username))
                .map(this::createUserDetails)
                .orElseThrow(NotFoundException::new);
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(UserEntity user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority().toString());
        return new User(
                String.valueOf(user.getId()),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
