package com.example.instagramproject.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class PostEntity {

    //id, user_id, date_time, like_count, comment_count, text

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "date_time")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private LocalDateTime dateTime;

    @Column(name = "lake_count")
    private Long likes;

    @Column(name = "comment_count")
    private Long comments;

    @Column(name = "text")
    private String text;
}
