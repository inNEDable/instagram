package com.example.instagramproject.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stories")
public class StoryEntity {

    // id, user_id, media, exp_date

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String media;

    @Column(name = "exp_date")
    private LocalDateTime expDate;
}
