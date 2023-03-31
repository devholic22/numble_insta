package com.numble.instagram.dto.message;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long user_id;
    private String content;
}
