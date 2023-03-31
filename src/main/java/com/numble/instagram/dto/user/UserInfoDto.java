package com.numble.instagram.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private String nickname;
    private String profile_image_url;
    private Long follower;
    private Long following;
}
