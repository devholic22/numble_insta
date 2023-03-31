package com.numble.instagram.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {

    private String nickname;
    private MultipartFile profile_image;
}
