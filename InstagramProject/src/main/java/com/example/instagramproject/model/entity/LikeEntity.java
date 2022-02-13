package com.example.instagramproject.model.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
public class LikeEntity {

    //id, user_id, date_time

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date_time")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private LocalDateTime dateTime;
}
