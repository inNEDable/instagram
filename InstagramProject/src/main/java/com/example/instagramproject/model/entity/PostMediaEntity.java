package com.example.instagramproject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "post_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaEntity {

    // id, url, post_id

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

//    @Column(name = "post_id")
//    private Long postId;

    @ManyToOne()
    @JoinColumn(name="post_id", nullable=false)
    private PostEntity post;
}
