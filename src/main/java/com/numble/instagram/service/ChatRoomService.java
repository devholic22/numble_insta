package com.numble.instagram.service;

import com.numble.instagram.dto.message.GetMessageDto;
import com.numble.instagram.dto.room.GetRoomDto;
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

import java.util.*;

@Service
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository,
                           MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public HashMap<String, ArrayList<GetRoomDto>> findAllMyRooms() {
        User loggedInUser = getLoggedInUser();
        ArrayList<ChatRoom> findAllByOpener_Id = chatRoomRepository.findAllByOpener_Id(loggedInUser.getId());
        ArrayList<ChatRoom> findAllByJoiner_Id = chatRoomRepository.findAllByJoiner_Id(loggedInUser.getId());
        ArrayList<ChatRoom> allRooms = new ArrayList<>();
        HashMap<String, ArrayList<GetRoomDto>> result = new HashMap<>();
        ArrayList<GetRoomDto> rooms = new ArrayList<>();

        allRooms.addAll(findAllByOpener_Id);
        allRooms.addAll(findAllByJoiner_Id);

        for (ChatRoom room : allRooms) {
            ArrayList<Message> messages = messageRepository.findAllByRoomId(room.getId());
            Message message = messages.get(messages.size() - 1);
            GetRoomDto roomDto = GetRoomDto.builder()
                    .id(room.getId())
                    .nickname(message.getSender().getNickname())
                    .profile_image(message.getSender().getProfile_image())
                    .last_message(message.getContent())
                    .last_send_at(message.getSend_at())
                    .build();
            rooms.add(roomDto);
        }
        result.put("chat_rooms", rooms);
        return result;
    }

    public HashMap<String, List<GetMessageDto>> findRoom(Long chat_room_id) {
        User loggedInUser = getLoggedInUser();
        ChatRoom targetRoom = chatRoomRepository.findByIdAndOpener_Id(chat_room_id, loggedInUser.getId());
        if (targetRoom == null) {
            targetRoom = chatRoomRepository.findByIdAndJoiner_Id(chat_room_id, loggedInUser.getId());
        }
        if (targetRoom == null) {
            throw new RuntimeException("방이 없거나 권한이 없습니다.");
        }
        // 메시지들의 리스트를 보내야 한다.
        ArrayList<Message> allMessage = messageRepository.findAllByRoomId(chat_room_id);
        HashMap<String, List<GetMessageDto>> result = new HashMap<>();
        ArrayList<GetMessageDto> allMessages = new ArrayList<>();
        for (Message targetMessage : allMessage) {
            Optional<User> writer = userRepository.findById(targetMessage.getSender().getId());
            GetMessageDto message = GetMessageDto.builder()
                    .id(targetMessage.getId())
                    .nickname(writer.get().getNickname())
                    .profile_image_url(writer.get().getProfile_image())
                    .content(targetMessage.getContent())
                    .sent_at(targetMessage.getSend_at())
                    .build();
            allMessages.add(message);
        }
        result.put("messages", allMessages);
        return result;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return userRepository.findByNickname(nickname);
    }
}
