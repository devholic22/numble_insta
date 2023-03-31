package com.numble.instagram.dto.reply;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditReplyDto {

    private Long id;
    private String content;
}
