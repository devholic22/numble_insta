package com.numble.instagram.controller;

import com.numble.instagram.dto.jwt.TokenDto;
import com.numble.instagram.dto.user.EditUserDto;
import com.numble.instagram.dto.user.LoginDto;
import com.numble.instagram.dto.user.UserDto;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.AlreadyExitedUserException;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.exception.NotLoggedInException;
import com.numble.instagram.exception.NotSearchedTargetException;
import com.numble.instagram.service.UserService;
import com.numble.instagram.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final UserUtil userUtil;

    public UserController(UserService userService, UserUtil userUtil) {
        this.userService = userService;
        this.userUtil = userUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@ModelAttribute UserDto userDto) {
        User newUser;
        try {
            newUser = userService.signup(userDto);
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @GetMapping("/profile/{user_id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long user_id) {
        try {
            return ResponseEntity.ok(userService.getProfile(user_id));
        } catch (NotSearchedTargetException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<User> editProfile(@ModelAttribute EditUserDto editUserDto) {
        return ResponseEntity.ok(userService.edit(editUserDto));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfile() {
        try {
            userService.delete(userUtil.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotLoggedInException | AlreadyExitedUserException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
