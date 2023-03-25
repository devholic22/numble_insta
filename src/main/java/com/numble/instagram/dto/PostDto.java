package com.numble.instagram.dto;

import com.numble.instagram.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private String content;

    private String image_url;
}
