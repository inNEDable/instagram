package com.example.instagramproject.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLikeDTO {

    private Long id;

    private Long userId;

    private Long postId;

    private Long commentId;
}
