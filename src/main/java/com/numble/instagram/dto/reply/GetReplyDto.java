package com.numble.instagram.dto.reply;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReplyDto {

    private Long id;
    private String content;
    private String nickname;
    private String profile_image_url;
}
