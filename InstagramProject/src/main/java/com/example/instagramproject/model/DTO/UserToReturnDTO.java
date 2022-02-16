package com.example.instagramproject.model.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserToReturnDTO {

    private Long id;

    private String username;

    private String fullName;

    private String email;

}
