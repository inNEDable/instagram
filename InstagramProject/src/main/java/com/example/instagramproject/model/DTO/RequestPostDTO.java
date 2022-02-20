package com.example.instagramproject.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostDTO {

    // User creating the post
    private Long userId;

    // Post id
    private Long postId;

    // Text of the post
    private String text;

}
