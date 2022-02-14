package com.example.instagramproject.model.DTO;

import com.example.instagramproject.model.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;


@Data
public class UserToRegisterDTO {

    private String username;

    private String fullName;

    private String email;

    private String password;

    private String confirmPassword;

    public UserEntity toEntity() {
        return new UserEntity(this.username, this.email, this.password, this.fullName);
    }
}
