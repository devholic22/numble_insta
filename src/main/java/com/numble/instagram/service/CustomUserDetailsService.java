package com.numble.instagram.service;

import com.numble.instagram.entity.User;
import com.numble.instagram.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User existUser = userRepository.findByNickname(nickname);
        if (existUser == null) {
            throw new UsernameNotFoundException("데이터베이스에서 찾을 수 없습니다.");
        }
        return createUser(nickname, existUser);
    }

    private org.springframework.security.core.userdetails.User createUser(String nickname, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(nickname + " -> 활성화되어 있지 않습니다.");
        }
        return new org.springframework.security.core.userdetails.User(nickname,
                null,
                new ArrayList<>());
    }
}