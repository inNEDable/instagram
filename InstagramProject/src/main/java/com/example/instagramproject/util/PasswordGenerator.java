package com.example.instagramproject.util;

public class PasswordGenerator {

    private static final String UPPER_CASES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SYMBOLS = "@#$%";
    private static final String DIGITS = "0123456789";
    private static final String LOWER_CASES = "abcdefghijklmnopqrstuvwxyz";

    public static String makePassword(int length) {
        String password = "";

        for (int i = 0; i < length - 3; i++) {
            password += randomCharacter(LOWER_CASES);
        }
        String randomDigit = randomCharacter(DIGITS);
        password = insertAtRandom(password, randomDigit);
        String randomCharacter = randomCharacter(SYMBOLS);
        password = insertAtRandom(password, randomCharacter);
        String upperCase = randomCharacter(UPPER_CASES);
        password = insertAtRandom(password, upperCase);

        return password;
    }

    private static String randomCharacter(String characters) {
        int n = characters.length();
        int index = (int) (n * Math.random());
        return characters.substring(index, index + 1);
    }

    private static String insertAtRandom(String str, String toInsert) {
        int n = str.length();
        int index = (int) ((n + 1) * Math.random());
        return str.substring(0, index) + toInsert + str.substring(index);
    }
}
