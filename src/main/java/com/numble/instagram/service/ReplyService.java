package com.numble.instagram.service;

import com.numble.instagram.dto.reply.EditReplyDto;
import com.numble.instagram.dto.reply.ReplyDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.Reply;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.CommentRepository;
import com.numble.instagram.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Reply write(ReplyDto replyDto, User writer) {

        if (!writer.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        if (replyDto.getComment_id() == null || replyDto.getContent() == null) {
            throw new NotQualifiedDtoException("comment_id 또는 content가 비어있습니다.");
        }

        Comment targetComment = commentRepository.findById(replyDto.getComment_id())
                .orElseThrow(() -> new NotSearchedTargetException("해당 댓글이 없습니다."));
        
        Reply newReply = Reply.builder()
                .content(replyDto.getContent())
                .writer(writer)
                .comment(targetComment)
                .build();

        return replyRepository.save(newReply);
    }

    public Reply edit(EditReplyDto editReplyDto, Long id, User writer) {

        if (!writer.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        if (editReplyDto.getContent() == null) {
            throw new NotQualifiedDtoException("content가 비어있습니다.");
        }

        Reply targetReply = replyRepository.findById(id)
                .orElseThrow(() -> new NotSearchedTargetException("해당 답글이 없습니다."));

        if (!targetReply.getWriter().equals(writer)) {
            throw new NotPermissionException("답글을 수정할 수 없습니다.");
        }

        targetReply.setContent(editReplyDto.getContent());

        return targetReply;
    }

    public void delete(Long id, User writer) {

        if (!writer.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        Reply targetReply = replyRepository.findById(id)
                .orElseThrow(() -> new NotSearchedTargetException("해당 답글이 없습니다."));

        if (!targetReply.getWriter().equals(writer)) {
            throw new NotPermissionException("답글을 삭제할 수 없습니다.");
        }

        replyRepository.deleteById(id);
    }
}
