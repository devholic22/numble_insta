package com.numble.instagram.controller;

import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.service.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> myRooms() {
        try {
            return ResponseEntity.ok(chatRoomService.findAllMyRooms());
        }  catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @GetMapping("/{chat_room_id}")
    @ResponseBody
    public ResponseEntity<?> room(@PathVariable Long chat_room_id) {
        try {
            return ResponseEntity.ok(chatRoomService.findRoom(chat_room_id));
        }  catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
