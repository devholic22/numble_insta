package com.numble.instagram.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private String content;

    private MultipartFile image;
}
