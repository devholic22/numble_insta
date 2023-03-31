package com.numble.instagram.controller;

import com.numble.instagram.dto.*;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
        } catch (RuntimeException e) {
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
    public ResponseEntity<?> deleteProfile(@RequestBody DeleteUserDto deleteUserDto) {
        try {
            userService.delete(deleteUserDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
