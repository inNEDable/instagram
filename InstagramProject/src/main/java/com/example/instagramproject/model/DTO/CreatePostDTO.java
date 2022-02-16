package com.example.instagramproject.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDTO {

    // User creating the post
    private Long userId;

    // Time of post creation
    private LocalDateTime dateTime;

    // Text of the post
    private String text;

    // URL of the post MEDIA
    private Set<String> media;
}
