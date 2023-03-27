package com.numble.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reply")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "writer_id")
    private User writer;
}
