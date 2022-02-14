package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.DTO.UserToRegisterDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    @Autowired
    private UserRepository userRepository;

    public UserToReturnDTO registerUser(UserToRegisterDTO userToRegisterDTO) {
        String username = userToRegisterDTO.getUsername();
        String email = userToRegisterDTO.getEmail();
        String password = userToRegisterDTO.getPassword();
        String confirmPassword = userToRegisterDTO.getConfirmPassword();

        if (username.isBlank()) throw new InvalidUserData("Username can't be blank!");
        if (!password.equals(confirmPassword)) throw new InvalidUserData("Passwords don't match");
        if (!patternMatches(email)) throw new InvalidUserData("Invalid email");
        if (userRepository.findUserEntityByUsername(username) != null) throw new InvalidUserData("Username already exist");

        UserEntity userEntity;
        if (userRepository.findUserEntityByEmail(email) == null){
            userEntity = userToRegisterDTO.toEntity();
            userRepository.save(userEntity);
        } else throw new InvalidUserData("Email already exist");


        //TODO: sendEmailVerification(userEntity.getEmail()); Class.sendEmail("http://123.453.456.45:8080/confirm/" + userEntity.getId())


        return UserToReturnDTO.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .build();
    }

    public void removeUserById(long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
    }

    public UserEntity getById(long id) {

        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isPresent()) {
            return optionalUserEntity.get();
        }
        else {
            throw new InvalidUserData("User not found");
        }
    }

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(EMAIL_REGEX)
                .matcher(emailAddress)
                .matches();
    }

    public UserEntity loginWithEmail(String email, String password) {
        UserEntity userEntity = userRepository.findUserEntityByEmailAndPassword(email, password);
        if (userEntity == null) throw new InvalidUserData("Invalid email or password");
        return userEntity;
    }

    public UserEntity loginWithUsername(String username, String password) {
        UserEntity userEntity = userRepository.findUserEntityByUsernameAndPassword(username, password);
        if (userEntity == null) throw new InvalidUserData("Invalid username or password");
        return userEntity;
    }
}
