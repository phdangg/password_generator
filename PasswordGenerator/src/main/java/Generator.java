import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Generator {
    private static final String DEFAULT_CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?/~`";
    public static String getPassword(String email, String masterPassword, int PASSWORD_LENGTH)
            throws NoSuchAlgorithmException, NoSuchProviderException{
        return getPassword(email,masterPassword,PASSWORD_LENGTH,DEFAULT_CHARACTER_SET);
    }

    public static String getPassword(String email, String masterPassword, int PASSWORD_LENGTH, String CHARACTER_SET)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        if (email == null || masterPassword == null) return null;

        // Perform PBKDF2 hashing
        String PBKDF2Secret = PBKDF2.getPBKDF2WithSalt(masterPassword);

        // Encode PBKDF2 result into special Base64
        byte[] PBKDF2Base64Encoded = Base64.encodeBase64(PBKDF2Secret.getBytes());


        // Make sure PBKDF2Base64Encoded not longer than 16 characters
        // Form the salt for BCrypt
        byte[] salt16Bytes = Salt.getSalt(PBKDF2Base64Encoded);

        // Perform BCrypt hashing
        byte[] bcryptHash = BCrypt.withDefaults().hash(10, salt16Bytes, masterPassword.getBytes(StandardCharsets.UTF_8));

        // Remove prefix and salt from BCrypt result to get the final byte array

        byte[] finalByteArray = new byte[bcryptHash.length-7];
        System.arraycopy(bcryptHash,7,finalByteArray,0,finalByteArray.length);

        // Deterministically choose characters from CHARACTER_SET
        StringBuilder passwordBuilder = new StringBuilder();
        for (byte b : finalByteArray) {
            int index = b % CHARACTER_SET.length();
            passwordBuilder.append(CHARACTER_SET.charAt(index));
            if (passwordBuilder.length() >= PASSWORD_LENGTH) {
                break;
            }
        }

        // Create a password template to ensure character group inclusion
        String template = "naxxxxxxxx"; // Example template with number and lowercase letter
        String shuffledTemplate = shuffleTemplate(template, reverseByteArray(finalByteArray));

        // Fill the template with chosen characters
        StringBuilder finalPassword = new StringBuilder();
        for (char c : shuffledTemplate.toCharArray()) {
            if (c == 'n') {
                finalPassword.append(chooseCharacterFromGroup("number",CHARACTER_SET));
            } else if (c == 'a') {
                finalPassword.append(chooseCharacterFromGroup("lowercase",CHARACTER_SET));
            } else {
                finalPassword.append(chooseCharacterFromGroup("any",CHARACTER_SET));
            }
        }

        return finalPassword.toString();

    }
    private static String shuffleTemplate(String template, byte[] reversedByteArray) {
        List<Character> characters = new ArrayList<>();
        for (char c : template.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters, new SecureRandom(reversedByteArray));
        StringBuilder shuffledTemplate = new StringBuilder();
        for (char c : characters) {
            shuffledTemplate.append(c);
        }
        return shuffledTemplate.toString();
    }

    private static byte[] reverseByteArray(byte[] byteArray) {
        byte[] reversed = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            reversed[i] = byteArray[byteArray.length - 1 - i];
        }
        return reversed;
    }

    private static char chooseCharacterFromGroup(String group, String CHARACTER_SET) {
        SecureRandom random = new SecureRandom();
        switch (group) {
            case "number":
                return CHARACTER_SET.charAt(52 + random.nextInt(10)); // Numbers are at indices 52-61
            case "lowercase":
                return CHARACTER_SET.charAt(26 + random.nextInt(26)); // Lowercase letters are at indices 26-51
            default:
                return CHARACTER_SET.charAt(random.nextInt(CHARACTER_SET.length())); // Any character from the set
        }
    }

}
