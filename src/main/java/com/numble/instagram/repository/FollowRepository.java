package com.numble.instagram.repository;

import com.numble.instagram.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findBySender_IdAndReceiver_Id(Long sender_id, Long receiver_id);
    void deleteBySender_IdAndReceiver_Id(Long sender_id, Long receiver_id);

    List<Follow> findAllBySender_id(Long sender_id);
    List<Follow> findAllByReceiver_id(Long receiver_id);
}
