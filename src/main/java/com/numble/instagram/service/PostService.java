package com.numble.instagram.service;

import com.numble.instagram.dto.PostDto;
import com.numble.instagram.entity.Post;
import com.numble.instagram.entity.User;
import com.numble.instagram.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post write(PostDto postDto, User writer) {

        Post newPost = Post.builder()
                .content(postDto.getContent())
                .image_url(postDto.getImage_url().getOriginalFilename())
                .writer(writer)
                .build();
        return postRepository.save(newPost);
    }

    public Post edit(Post post, PostDto postDto) {
        post.setContent(postDto.getContent());
        post.setImage_url(postDto.getImage_url().getOriginalFilename());

        return post;
    }

    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
}
