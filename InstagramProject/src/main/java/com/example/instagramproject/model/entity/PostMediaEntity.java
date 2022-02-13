package com.example.instagramproject.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "")
public class MediaEntity {

    //id, url, post_id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "post_id")
    private Long post_id;
}
