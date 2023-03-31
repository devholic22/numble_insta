package com.numble.instagram.repository;

import com.numble.instagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PostRepository extends JpaRepository<Post, Long> {

    ArrayList<Post> findAllByWriter_Id(Long writer_id);
}
