package com.numble.instagram.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentDto {

    private Long id;
    private String content;
    private String nickname;
    private String profile_image_url;
    private ArrayList<GetReplyDto> replies;
}
