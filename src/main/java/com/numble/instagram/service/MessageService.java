package com.numble.instagram.service;

import com.numble.instagram.dto.message.MessageDto;
import com.numble.instagram.entity.ChatRoom;
import com.numble.instagram.entity.Message;
import com.numble.instagram.entity.User;
import com.numble.instagram.repository.ChatRoomRepository;
import com.numble.instagram.repository.MessageRepository;
import com.numble.instagram.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository,
                          ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void send(MessageDto messageDto) {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser.getId() == messageDto.getUser_id()) {
            throw new RuntimeException("자기 자신과는 채팅할 수 없습니다.");
        }
        Optional<User> targetUser = userRepository.findById(messageDto.getUser_id());
        if (targetUser.isEmpty()) {
            throw new RuntimeException("없는 사용자 입니다.");
        }
        ChatRoom existRoom = chatRoomRepository.findByOpener_IdAndJoiner_Id(loggedInUser.getId(), targetUser.get().getId());
        if (existRoom == null) {
            existRoom = chatRoomRepository.findByOpener_IdAndJoiner_Id(targetUser.get().getId(), loggedInUser.getId());
            if (existRoom == null) {
                // 방 생성 (loggedInUser가 opener, targetUser가 joiner)
                ChatRoom newChatRoom = ChatRoom.builder()
                        .opener(loggedInUser)
                        .joiner(targetUser.get())
                        .build();
                chatRoomRepository.save(newChatRoom);
                existRoom = newChatRoom;
            }
        }
        // 방에 메시지 추가
        Message newMessage = Message.builder()
                .room(existRoom)
                .sender(loggedInUser)
                .receiver(targetUser.get())
                .content(messageDto.getContent())
                .send_at(new Timestamp(System.currentTimeMillis()))
                .build();
        messageRepository.save(newMessage);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return userRepository.findOneWithAuthoritiesByNickname(nickname).get();
    }
}
