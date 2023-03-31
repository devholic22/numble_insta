package com.numble.instagram.repository;

import com.numble.instagram.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    ArrayList<Reply> findAllByComment_Id(Long comment_id);
}
