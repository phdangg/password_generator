import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;


public class Generator {
    private byte[] hash;
    private int passwordLength;
    private static final String DEFAULT_CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?/~`";

    Generator(String masterPassword, int passwordLength, String ...metadata){
        this.hash = hashing(masterPassword,metadata);
        this.passwordLength = passwordLength;
    }
    private byte[] hashing(String masterPassword, String ...metadata){
        byte[] salt = Salt.getSalt(metadata);

        // Perform PBKDF2 hashing
        byte[] PBKDF2Secret = PBKDF2.getPBKDF2WithSalt(masterPassword,salt);

        // Make sure PBKDF2Base64Encoded not longer than 16 characters
        // Form the salt for BCrypt
        byte[] salt16Bytes = Salt.getSalt16Bytes(PBKDF2Secret);

        // Perform BCrypt hashing
        return BCrypt.withDefaults().hash(10, salt16Bytes, masterPassword.getBytes(StandardCharsets.UTF_8));
    }
    //A template is a string generated for every password based
    //on the character set. E.g., a password with at least one
    //number, one lowercase character and a length of 10.
    // Whereas "n" denotes a number, "a" denotes a lowercase
    // letter and x denotes any symbol from the
    //character set.
    private static String generateTemplate(int PASSWORD_LENGTH){
        StringBuilder template = new StringBuilder();

        return template.toString();
    }

    public String getPassword(String email, String masterPassword, int PASSWORD_LENGTH, String CHARACTER_SET) {
        if (email == null || masterPassword == null) return null;
        if (CHARACTER_SET.equals("d"))
            CHARACTER_SET = DEFAULT_CHARACTER_SET;

        // Get salt


        // Remove prefix and salt from BCrypt result to get the final byte array

        byte[] finalByteArray = new byte[hash.length-7];
        System.arraycopy(hash,7,finalByteArray,0,finalByteArray.length);

        // Fill the template with chosen characters
        StringBuilder finalPassword = new StringBuilder();

        // Deterministically choose characters from CHARACTER_SET
        for (byte b : finalByteArray) {
            if (finalPassword.length() > PASSWORD_LENGTH - 1) {
                break;
            }
            int index = b % CHARACTER_SET.length();
            finalPassword.append(CHARACTER_SET.charAt(index));
        }

        return finalPassword.toString();

    }

}
