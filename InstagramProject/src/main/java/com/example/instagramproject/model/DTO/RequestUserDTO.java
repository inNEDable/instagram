package com.example.instagramproject.model.DTO;

import com.example.instagramproject.model.entity.UserEntity;
import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestUserDTO {

    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String password;

    private String newPassword;

    private String confirmPassword;

    private String profilePicture;

    private String phoneNumber;

    private String gender;

    private LocalDate birthDate;

    private String website;

    private String bio;

    public UserEntity toEntity() {
        return new UserEntity(this.username, this.email, this.password, this.fullName);
    }
}
