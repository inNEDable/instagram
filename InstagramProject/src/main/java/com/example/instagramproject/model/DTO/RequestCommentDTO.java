package com.example.instagramproject.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCommentDTO {

    private Long id;

    // User creating the comment
    private Long userId;

    // Comment created in post
    private Long postId;

    // Text of the comment
    private String text;

}
