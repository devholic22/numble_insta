package com.numble.instagram.controller;

import com.numble.instagram.dto.message.MessageDto;
import com.numble.instagram.exception.*;
import com.numble.instagram.service.MessageService;
import com.numble.instagram.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final UserUtil userUtil;

    public MessageController(MessageService messageService, UserUtil userUtil) {
        this.messageService = messageService;
        this.userUtil = userUtil;
    }

    @PostMapping
    public ResponseEntity<?> send(@RequestBody MessageDto messageDto) {
        try {
            messageService.send(messageDto, userUtil.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ExitedUserException |
                 NotQualifiedDtoException |
                 SelfMessageException |
                 NotSearchedTargetException |
                 ExitedTargetUserException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
