package com.numble.instagram.service;

import com.numble.instagram.dto.ReplyDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.Reply;
import com.numble.instagram.repository.CommentRepository;
import com.numble.instagram.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    public ReplyService(ReplyRepository replyRepository,
                        CommentRepository commentRepository) {
        this.replyRepository = replyRepository;
        this.commentRepository = commentRepository;
    }

    public Reply write(ReplyDto replyDto) {
        Optional<Comment> targetComment = commentRepository.findById(replyDto.getComment_id());
        if (targetComment.isEmpty()) {
            throw new RuntimeException("해당 댓글이 없습니다.");
        }
        Reply newReply = Reply.builder()
                .content(replyDto.getContent())
                .comment(targetComment.get())
                .build();
        return replyRepository.save(newReply);
    }
}
