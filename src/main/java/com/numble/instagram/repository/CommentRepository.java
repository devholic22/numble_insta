package com.numble.instagram.repository;

import com.numble.instagram.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    ArrayList<Comment> findAllByPost_Id(Long post_id);
}
