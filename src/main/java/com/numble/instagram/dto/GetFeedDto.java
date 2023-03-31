package com.numble.instagram.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFeedDto {

    private Long id;
    private String content;
    private String image_url;
    private ArrayList<GetCommentDto> comments;
}
