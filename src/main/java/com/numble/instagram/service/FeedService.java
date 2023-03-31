package com.numble.instagram.service;

import com.numble.instagram.dto.GetCommentDto;
import com.numble.instagram.dto.GetFeedDto;
import com.numble.instagram.dto.GetReplyDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.Post;
import com.numble.instagram.entity.Reply;
import com.numble.instagram.entity.User;
import com.numble.instagram.repository.CommentRepository;
import com.numble.instagram.repository.PostRepository;
import com.numble.instagram.repository.ReplyRepository;
import com.numble.instagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public FeedService(PostRepository postRepository,
                       UserRepository userRepository,
                       CommentRepository commentRepository,
                       ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }

    // Chat GPT로 리팩터링한 것이라서 공부 필요함.
    public HashMap<String, ArrayList<GetFeedDto>> getFeed(Long user_id) {
        Optional<User> writer = userRepository.findById(user_id);
        if (writer.isEmpty()) {
            throw new RuntimeException("해당 유저가 없습니다.");
        }

        List<Post> feeds = postRepository.findAllByWriter_Id(user_id);

        List<GetFeedDto> feedResult = feeds.stream().map(feed -> {
            GetFeedDto feedDto = GetFeedDto.builder()
                    .id(feed.getId())
                    .content(feed.getContent())
                    .image_url(feed.getImage_url())
                    .build();

            List<Comment> comments = commentRepository.findAllByPost_Id(feed.getId());

            List<GetCommentDto> resultComment = comments.stream().map(comment -> {
                GetCommentDto commentDto = GetCommentDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .nickname(comment.getWriter().getNickname())
                        .profile_image_url(comment.getWriter().getProfile_image())
                        .build();

                List<Reply> replies = replyRepository.findAllByComment_Id(comment.getId());

                List<GetReplyDto> resultReply = replies.stream().map(reply -> {
                    return GetReplyDto.builder()
                            .id(reply.getId())
                            .content(reply.getContent())
                            .nickname(reply.getWriter().getNickname())
                            .profile_image_url(reply.getWriter().getProfile_image())
                            .build();
                }).collect(Collectors.toList());

                commentDto.setReplies((ArrayList<GetReplyDto>) resultReply);
                return commentDto;
            }).collect(Collectors.toList());

            feedDto.setComments((ArrayList<GetCommentDto>) resultComment);
            return feedDto;
        }).toList();

        HashMap<String, ArrayList<GetFeedDto>> result = new HashMap<>();
        result.put("posts", new ArrayList<>(feedResult));
        return result;
    }

}
