package com.numble.instagram.service;

import com.numble.instagram.dto.message.MessageDto;
import com.numble.instagram.entity.ChatRoom;
import com.numble.instagram.entity.Message;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.ChatRoomRepository;
import com.numble.instagram.repository.MessageRepository;
import com.numble.instagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

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

    public void send(MessageDto messageDto, User sender) {

        if (!sender.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        if (messageDto.getUser_id() == null || messageDto.getContent() == null) {
            throw new NotQualifiedDtoException("user_id 또는 content가 비어있습니다.");
        }

        if (sender.getId().equals(messageDto.getUser_id())) {
            throw new SelfMessageException("자기 자신과는 채팅할 수 없습니다.");
        }

        User targetUser = userRepository.findById(messageDto.getUser_id())
                .orElseThrow(() -> new NotSearchedTargetException("없는 사용자 입니다."));

        if (!targetUser.isActivated()) {
            throw new ExitedTargetUserException("탈퇴한 유저라 불가능합니다.");
        }

        ChatRoom existRoom = findChatRoom(sender, targetUser);

        if (existRoom == null) {
            ChatRoom newChatRoom = ChatRoom.builder()
                    .opener(sender)
                    .joiner(targetUser)
                    .build();
            chatRoomRepository.save(newChatRoom);
            existRoom = newChatRoom;
        }

        // 방에 메시지 추가
        Message newMessage = Message.builder()
                .room(existRoom)
                .sender(sender)
                .receiver(targetUser)
                .content(messageDto.getContent())
                .send_at(new Timestamp(System.currentTimeMillis()))
                .build();

        messageRepository.save(newMessage);
    }

    private ChatRoom findChatRoom(User sender, User targetUser) {
        ChatRoom existRoom = chatRoomRepository.findByOpener_IdAndJoiner_Id(sender.getId(), targetUser.getId());
        if (existRoom == null) {
            existRoom = chatRoomRepository.findByOpener_IdAndJoiner_Id(targetUser.getId(), sender.getId());
        }
        return existRoom;
    }
}
