package com.example.instagramproject.model.DTO;

import com.example.instagramproject.model.entity.UserEntity;
import lombok.Builder;
import lombok.Data;


@Data
public class UserToRegisterDTO {

    private String username;

    private String fullName;

    //@Email(regexp=".*@.*\\..*", message = "Email should be valid")
    private String email;

    private String password;

    public UserEntity toEntity() {
        return new UserEntity(this.username, this.email, this.password, this.fullName);
    }
}
