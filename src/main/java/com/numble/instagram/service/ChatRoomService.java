package com.numble.instagram.service;

import com.numble.instagram.dto.message.GetMessageDto;
import com.numble.instagram.dto.room.GetRoomDto;
import com.numble.instagram.entity.ChatRoom;
import com.numble.instagram.entity.Message;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.ChatRoomException;
import com.numble.instagram.exception.ExitedUserException;
import com.numble.instagram.repository.ChatRoomRepository;
import com.numble.instagram.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    public HashMap<String, ArrayList<GetRoomDto>> findAllMyRooms(User loggedInUser) {

        if (!loggedInUser.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        List<ChatRoom> allRooms = Stream.concat(
                chatRoomRepository.findAllByOpener_Id(loggedInUser.getId()).stream(),
                chatRoomRepository.findAllByJoiner_Id(loggedInUser.getId()).stream()).toList();

        ArrayList<GetRoomDto> rooms = allRooms.stream().map(room -> {
            List<Message> messages = messageRepository.findAllByRoomId(room.getId());
            Message lastMessage = messages.get(messages.size() - 1);
            return GetRoomDto.builder()
                    .id(room.getId())
                    .nickname(lastMessage.getSender().getNickname())
                    .profile_image(lastMessage.getSender().getProfile_image())
                    .last_message(lastMessage.getContent())
                    .last_send_at(lastMessage.getSend_at())
                    .build();
        }).collect(Collectors.toCollection(ArrayList::new));

        HashMap<String, ArrayList<GetRoomDto>> result = new HashMap<>();
        result.put("chat_rooms", rooms);
        return result;
    }

    public HashMap<String, List<GetMessageDto>> findRoom(Long chatRoomId, User loggedInUser) {

        if (!loggedInUser.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        ChatRoom targetRoom = findChatRoomByIdAndUser(chatRoomId, loggedInUser);
        if (targetRoom == null) {
            throw new ChatRoomException("방이 없거나 권한이 없습니다.");
        }

        List<Message> allMessage = messageRepository.findAllByRoomId(chatRoomId);
        List<GetMessageDto> allMessages = allMessage.stream()
                .map(message -> {
                    User writer = message.getSender();
                    return GetMessageDto.builder()
                            .id(message.getId())
                            .nickname(writer.getNickname())
                            .profile_image_url(writer.getProfile_image())
                            .content(message.getContent())
                            .sent_at(message.getSend_at())
                            .build();
                })
                .collect(Collectors.toList());

        HashMap<String, List<GetMessageDto>> result = new HashMap<>();
        result.put("messages", allMessages);

        return result;
    }

    private ChatRoom findChatRoomByIdAndUser(Long chatRoomId, User loggedInUser) {
        ChatRoom existRoom = chatRoomRepository.findByIdAndOpener_Id(chatRoomId, loggedInUser.getId());
        if (existRoom == null) {
            existRoom = chatRoomRepository.findByIdAndJoiner_Id(chatRoomId, loggedInUser.getId());
        }
        return existRoom;
    }
}
