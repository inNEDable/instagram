package com.example.instagramproject.util;

public class PasswordGenerator {

    private static final String UPPER_CASES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SYMBOLS = "@#$%";
    private static final String DIGITS = "0123456789";
    private static final String LOWER_CASES = "abcdefghijklmnopqrstuvwxyz";
    public static final int PASSWORD_LENGTH = 20;

    public static String makePassword() {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH - 3; i++) {
            password.append(randomCharacter(LOWER_CASES));
        }
        String resultPas = password.toString();
        String randomDigit = randomCharacter(DIGITS);
        resultPas = insertAtRandom(resultPas, randomDigit);
        String randomCharacter = randomCharacter(SYMBOLS);
        resultPas = insertAtRandom(resultPas, randomCharacter);
        String upperCase = randomCharacter(UPPER_CASES);
        resultPas = insertAtRandom(resultPas, upperCase);

        return resultPas;
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
