package com.numble.instagram.service;

import com.numble.instagram.dto.*;
import com.numble.instagram.entity.Authority;
import com.numble.instagram.entity.User;
import com.numble.instagram.jwt.JwtFilter;
import com.numble.instagram.jwt.TokenProvider;
import com.numble.instagram.repository.FollowRepository;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.util.SecurityUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final FollowRepository followRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                       FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.followRepository = followRepository;
    }

    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByNickname(userDto.getNickname()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .profile_image(userDto.getProfile_image())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();
        return userRepository.save(user);
    }

    public ResponseEntity<TokenDto> login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getNickname(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findOneWithAuthoritiesByNickname(userDetails.getUsername()).get();
        String jwt = tokenProvider.createToken(authentication, user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    public User edit(EditUserDto editUserDto) {
        User loggedInUser = getLoggedInUser();
        loggedInUser.setNickname(editUserDto.getNickname());
        loggedInUser.setProfile_image(editUserDto.getProfile_image());
        return loggedInUser;
    }

    public UserInfoDto getProfile(Long user_id) {
        Optional<User> targetUser = userRepository.findById(user_id);
        if (targetUser.isEmpty()) {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
        Long follower = (long) followRepository.findAllByReceiver_id(targetUser.get().getId()).size();
        Long following = (long) followRepository.findAllBySender_id(targetUser.get().getId()).size();
        return UserInfoDto.builder().
                nickname(targetUser.get().getNickname())
                .profile_image_url(targetUser.get().getProfile_image())
                .follower(follower)
                .following(following)
                .build();
    }

    public void delete(DeleteUserDto deleteUserDto) {
        User loggedInUser = getLoggedInUser();
        Optional<User> targetUser = userRepository.findById(deleteUserDto.getUser_id());
        if (targetUser.isEmpty()) {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
        if (!loggedInUser.equals(targetUser.get())) {
            throw new RuntimeException("탈퇴할 권한이 없습니다.");
        }
        loggedInUser.setActivated(false);
        loggedInUser.setProfile_image("default_image.png");
        loggedInUser.setNickname("deleted_user");
        loggedInUser.setAuthorities(new HashSet<>());
    }

    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByNickname(username);
    }

    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByNickname);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return userRepository.findOneWithAuthoritiesByNickname(nickname).get();
    }
}
