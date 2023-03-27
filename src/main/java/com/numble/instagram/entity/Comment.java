package com.numble.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private Post post;
}
