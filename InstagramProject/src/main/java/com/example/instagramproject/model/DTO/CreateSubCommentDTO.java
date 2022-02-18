package com.example.instagramproject.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubCommentDTO {

    private Long id;

    // User creating the comment
    private Long userId;

    // Text of the comment
    private String text;

    // Comment created in comment
    private Long parentCommentId;
}
