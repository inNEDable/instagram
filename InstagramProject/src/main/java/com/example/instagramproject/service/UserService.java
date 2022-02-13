package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.EmailExists;
import com.example.instagramproject.exceptions.InvalidEmail;
import com.example.instagramproject.exceptions.UsernameExists;
import com.example.instagramproject.model.DTO.UserToRegisterDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    // ^(.+)@(\\S+)$
    //.*@.*\\..*
    @Autowired
    private UserRepository userRepository;

    public UserToReturnDTO registerUser(UserToRegisterDTO userToRegisterDTO) {
        if (!patternMatches(userToRegisterDTO.getEmail())) throw new InvalidEmail("Invalid email");

        UserEntity userEntity;
        if (userRepository.findAllByUsername(userToRegisterDTO.getUsername()).isEmpty()){
            if (userRepository.findAllByEmail(userToRegisterDTO.getEmail()).isEmpty()){
                 userEntity = userToRegisterDTO.toEntity();
                userRepository.save(userEntity);
            } else throw new EmailExists("Email already exist");
        } else throw new UsernameExists("Username already exist");

        //TODO: sendEmailVerification(userEntity.getEmail());

        return UserToReturnDTO.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .build();
    }

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(EMAIL_REGEX)
                .matcher(emailAddress)
                .matches();
    }
}
