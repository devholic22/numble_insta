package com.numble.instagram.service;

import com.numble.instagram.dto.jwt.TokenDto;
import com.numble.instagram.dto.user.*;
import com.numble.instagram.entity.Authority;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.*;
import com.numble.instagram.jwt.JwtFilter;
import com.numble.instagram.jwt.TokenProvider;
import com.numble.instagram.repository.FollowRepository;
import com.numble.instagram.repository.UserRepository;
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
                .profile_image(userDto.getProfile_image().getOriginalFilename())
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

    public User edit(EditUserDto editUserDto, User loggedInUser) {

        if (loggedInUser == null) {
            throw new NotLoggedInException("로그인되지 않았습니다.");
        }

        if (!loggedInUser.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        if (editUserDto.getNickname() == null || editUserDto.getProfile_image() == null) {
            throw new NotQualifiedDtoException("nickname 또는 profile_image가 비어있습니다.");
        }

        loggedInUser.setNickname(editUserDto.getNickname());
        loggedInUser.setProfile_image(editUserDto.getProfile_image().getOriginalFilename());
        return loggedInUser;
    }

    public UserInfoDto getProfile(Long userId) {

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotSearchedTargetException("존재하지 않는 유저입니다."));

        Long follower = (long) followRepository.findAllByReceiver_id(targetUser.getId()).size();
        Long following = (long) followRepository.findAllBySender_id(targetUser.getId()).size();

        return UserInfoDto.builder().
                nickname(targetUser.getNickname())
                .profile_image_url(targetUser.getProfile_image())
                .follower(follower)
                .following(following)
                .build();
    }

    public void delete(User loggedInUser) {

        if (loggedInUser == null) {
            throw new NotLoggedInException("로그인되지 않았습니다.");
        }

        if (!loggedInUser.isActivated()) {
            throw new AlreadyExitedUserException("이미 탈퇴한 유저입니다.");
        }

        loggedInUser.setActivated(false);
        loggedInUser.setProfile_image("default_image.png");
        loggedInUser.setNickname("deleted_user");
        loggedInUser.setAuthorities(new HashSet<>());
    }
}
