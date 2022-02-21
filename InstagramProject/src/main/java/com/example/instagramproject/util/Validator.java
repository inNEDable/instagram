package com.example.instagramproject.util;

import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.model.repository.UserRepository;

import java.util.regex.Pattern;

public class Validator {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String WEBSITE_REGEX = "^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*$";

    public static void validateUsernameExists(UserRepository userRepository, String username) {
        if (userRepository.findUserEntityByUsername(username).isPresent()) {
            throw new InvalidData("This username isn't available. Please try another");
        }
    }

    public static void validateEmailExists(UserRepository userRepository, String email) {
        if (userRepository.findUserEntityByEmail(email).isPresent()) {
            throw new InvalidData("Another account is using " + email);
        }
    }

    public static void validateEmailPatternMatches(String emailAddress) {
        if (!Pattern.compile(EMAIL_REGEX)
                .matcher(emailAddress)
                .matches()) throw new InvalidData("Invalid email");
    }

    public static void validateWebSitePatternMatches(String website) {
        if (!Pattern.compile(WEBSITE_REGEX)
                .matcher(website)
                .matches()) throw new InvalidData("Invalid website!");
    }

    public static void validateStringLength(Integer min, Integer max, String string) {
        if (min == null) min = 0;
        if (string.length() > max || string.length() < min){
            throw new InvalidData(string + " is out of acceptable length bounds");
        }

    }

    public static void validateGender(Character gender) {
        if (gender == 'f' || gender == 'm') return;
        throw new InvalidData("Invalid gender");
    }
}
