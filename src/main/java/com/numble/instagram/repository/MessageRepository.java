package com.numble.instagram.repository;

import com.numble.instagram.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface MessageRepository extends JpaRepository<Message, Long> {

    ArrayList<Message> findAllByRoomId(Long id);
}
