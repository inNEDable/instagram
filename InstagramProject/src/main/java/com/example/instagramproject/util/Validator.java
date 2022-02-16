package com.example.instagramproject.util;

import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.repository.UserRepository;

import java.util.regex.Pattern;

public class Validator {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String WEBSITE = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

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

    public static boolean validateEmailPatternMatches(String emailAddress) {
        return Pattern.compile(EMAIL_REGEX)
                .matcher(emailAddress)
                .matches();
    }

    public static boolean validateWebSitePatternMatches(String website) {
        return Pattern.compile(WEBSITE)
                .matcher(website)
                .matches();
    }
}
