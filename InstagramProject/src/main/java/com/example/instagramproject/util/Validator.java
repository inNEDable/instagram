package com.example.instagramproject.util;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.repository.UserRepository;

import java.util.regex.Pattern;

public class Validator {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String WEBSITE_REGEX = "^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*$";
    private static final String STRONG_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    /*
    ^                 # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once
    (?=\S+$)          # no whitespace allowed in the entire string
    .{8,}             # anything, at least eight places though
    $                 # end-of-string
     */

    public static void validateUsernameExists(UserRepository userRepository, String username) {
        if (userRepository.findUserEntityByUsername(username).isPresent()) {
            throw new InvalidDataException("This username isn't available. Please try another");
        }
    }

    public static void validateEmailExists(UserRepository userRepository, String email) {
        if (userRepository.findUserEntityByEmail(email).isPresent()) {
            throw new InvalidDataException("Another account is using " + email);
        }
    }

    public static void validateRealEmail(String emailAddress) {
        if (!Pattern.compile(EMAIL_REGEX)
                .matcher(emailAddress)
                .matches()) throw new InvalidDataException("Invalid email");
    }

    public static void validateRealWebSite(String website) {
        if (!Pattern.compile(WEBSITE_REGEX)
                .matcher(website)
                .matches()) throw new InvalidDataException("Invalid website!");
    }

    public static void validateStrongPassword(String website) {
        if (!Pattern.compile(STRONG_PASSWORD_REGEX)
                .matcher(website)
                .matches()) throw new InvalidDataException("Password is not strong enough");
    }

    public static void validateStringLength(Integer min, Integer max, String string) {
        if (min == null) min = 0;
        if (string.length() > max || string.length() < min){
            throw new InvalidDataException(string + " is out of acceptable length bounds");
        }

    }

    public static void validateGender(Character gender) {
        if (gender == 'f' || gender == 'm') return;
        throw new InvalidDataException("Invalid gender");
    }
}
