package com.numble.instagram.repository;

import com.numble.instagram.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findByOpener_IdAndJoiner_Id(Long opener_id, Long joiner_id);
    ChatRoom findByIdAndOpener_Id(Long id, Long opener_id);
    ChatRoom findByIdAndJoiner_Id(Long id, Long joiner_id);

    ArrayList<ChatRoom> findAllByOpener_Id(Long opener_id);
    ArrayList<ChatRoom> findAllByJoiner_Id(Long joiner_id);
}
