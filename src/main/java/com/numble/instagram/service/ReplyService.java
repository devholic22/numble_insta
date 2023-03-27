package com.numble.instagram.service;

import com.numble.instagram.dto.EditReplyDto;
import com.numble.instagram.dto.ReplyDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.Reply;
import com.numble.instagram.entity.User;
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

    public Reply write(ReplyDto replyDto, User loggedInUser) {
        Optional<Comment> targetComment = commentRepository.findById(replyDto.getComment_id());
        if (targetComment.isEmpty()) {
            throw new RuntimeException("해당 댓글이 없습니다.");
        }
        Reply newReply = Reply.builder()
                .content(replyDto.getContent())
                .writer(loggedInUser)
                .comment(targetComment.get())
                .build();
        return replyRepository.save(newReply);
    }

    public Reply edit(EditReplyDto editReplyDto, User loggedInUser) {
        Optional<Reply> targetReply = replyRepository.findById(editReplyDto.getId());
        if (targetReply.isEmpty()) {
            throw new RuntimeException("해당 댓글이 없습니다.");
        }
        if (!targetReply.get().getWriter().equals(loggedInUser)) {
            throw new RuntimeException("수정할 수 없습니다.");
        }
        targetReply.get().setContent(editReplyDto.getContent());
        return targetReply.get();
    }
}
