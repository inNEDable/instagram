package com.example.instagramproject.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tags")
public class TagEntity {

    // id, text

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @ManyToMany()
    @JoinTable(
            name = "posts_have_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<PostEntity> posts;


    /// Methods :

    @JsonBackReference
    public Set<PostEntity> getPosts() {
        return posts;
    }
}
