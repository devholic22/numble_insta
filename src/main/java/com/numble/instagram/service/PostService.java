package com.numble.instagram.service;

import com.numble.instagram.dto.post.PostDto;
import com.numble.instagram.entity.Post;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.NotLoggedInException;
import com.numble.instagram.exception.NotQualifiedDtoException;
import com.numble.instagram.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post write(PostDto postDto, User writer) {

        if (writer == null) {
            throw new NotLoggedInException("로그인되지 않았습니다.");
        }

        if (postDto.getContent() == null || postDto.getImage() == null) {
            throw new NotQualifiedDtoException("content 또는 image가 비었습니다.");
        }

        Post newPost = Post.builder()
                .content(postDto.getContent())
                .image_url(postDto.getImage().getOriginalFilename())
                .writer(writer)
                .build();

        return postRepository.save(newPost);
    }

    public Post edit(Post post, PostDto postDto) {
        post.setContent(postDto.getContent());
        post.setImage_url(postDto.getImage().getOriginalFilename());

        return post;
    }

    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
}
