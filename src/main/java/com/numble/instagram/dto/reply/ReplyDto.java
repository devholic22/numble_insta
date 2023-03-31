package com.numble.instagram.dto.reply;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto {

    private Long comment_id;
    private String content;
}
