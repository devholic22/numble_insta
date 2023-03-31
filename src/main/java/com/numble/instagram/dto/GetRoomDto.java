package com.numble.instagram.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoomDto {

    private Long id;
    private String nickname;
    private String profile_image;
    private String last_message;
    private Date last_send_at;
}
