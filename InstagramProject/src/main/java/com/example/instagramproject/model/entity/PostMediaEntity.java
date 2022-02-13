package com.example.instagramproject.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "post_media")
public class PostMediaEntity {

    //id, url, post_id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @Column(name = "post_id")
    private Long postId;
}
