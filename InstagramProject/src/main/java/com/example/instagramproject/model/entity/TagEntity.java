package com.example.instagramproject.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "tags")
public class TagEntity {

    //id, text

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;
}
