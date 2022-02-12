package com.example.instagramproject.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    //id, data_time, user_id, text, like_count, post_id, parent_comment_id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private LocalDateTime dateTime;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "text")
    private String text;

    @Column(name = "lake_count")
    private Long likes;

    @Column(name = "post_id")
    private int postID;

    @Column(name = "parent_comment_id")
    private int parentCommentId;

}
