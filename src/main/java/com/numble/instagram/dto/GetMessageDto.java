package com.numble.instagram.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMessageDto {

    private Long id;
    private String nickname;
    private String profile_image_url;
    private String content;
    private java.util.Date sent_at;
}
