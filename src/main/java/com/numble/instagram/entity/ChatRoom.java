package com.numble.instagram.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "opener_id")
    private User opener;

    @ManyToOne
    @JoinColumn(name = "joiner_id")
    private User joiner;
}
