package com.example.instagramproject.util;

import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.repository.UserRepository;

public class Validator {

    public static void validateUsernameExists(UserRepository userRepository, String username, String s) {
        if (userRepository.findUserEntityByUsername(username).isPresent()) {
            throw new InvalidUserData(s);
        }
    }

    public static void validateEmailExists(UserRepository userRepository, String email, String s) {
        if (userRepository.findUserEntityByEmail(email).isPresent()) {
            throw new InvalidUserData(s);
        }
    }
}
