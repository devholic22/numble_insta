package com.numble.instagram.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String nickname;

    private MultipartFile profile_image;

}