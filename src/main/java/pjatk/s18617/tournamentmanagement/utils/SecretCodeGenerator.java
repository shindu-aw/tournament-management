package pjatk.s18617.tournamentmanagement.utils;

import java.security.SecureRandom;

public final class SecretCodeGenerator {

    private SecretCodeGenerator() { // prevent instantiation
        throw new UnsupportedOperationException("Instantiating utility class...");
    }

    public static String generateSecretCode() {
        final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
        final int CODE_LENGTH = 8;
        final SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++)
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        return sb.toString();
    }

}
