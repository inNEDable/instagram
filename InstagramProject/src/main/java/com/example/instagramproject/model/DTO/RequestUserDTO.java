package com.example.instagramproject.model.DTO;

import com.example.instagramproject.model.entity.UserEntity;
import lombok.*;

import javax.validation.constraints.Email;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUserDTO {

    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String password;

    private String confirmPassword;


    public UserEntity toEntity() {
        return new UserEntity(this.username, this.email, this.password, this.fullName);
    }

    @Override
    public String toString() {
        return "UserToRegisterDTO{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
