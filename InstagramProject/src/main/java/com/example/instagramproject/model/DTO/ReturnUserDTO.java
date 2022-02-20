package com.example.instagramproject.model.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnUserDTO {

    private Long id;

    private String username;

    private String fullName;

    private String email;

}
