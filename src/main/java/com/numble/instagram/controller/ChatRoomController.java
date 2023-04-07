package com.numble.instagram.controller;

import com.numble.instagram.exception.ChatRoomException;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.exception.ExitedUserException;
import com.numble.instagram.exception.NotLoggedInException;
import com.numble.instagram.service.ChatRoomService;
import com.numble.instagram.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final UserUtil userUtil;

    public ChatRoomController(ChatRoomService chatRoomService, UserUtil userUtil) {
        this.chatRoomService = chatRoomService;
        this.userUtil = userUtil;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> myRooms() {
        try {
            return ResponseEntity.ok(chatRoomService.findAllMyRooms(userUtil.getLoggedInUser()));
        }  catch (NotLoggedInException | ExitedUserException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @GetMapping("/{chat_room_id}")
    @ResponseBody
    public ResponseEntity<?> room(@PathVariable Long chat_room_id) {
        try {
            return ResponseEntity.ok(chatRoomService.findRoom(chat_room_id, userUtil.getLoggedInUser()));
        }  catch (NotLoggedInException | ExitedUserException | ChatRoomException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
