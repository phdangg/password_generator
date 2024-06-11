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
    private static final String DEFAULT_TEMPLATE = "naxxxxxxxx";

    public static String getPassword(String email, String masterPassword, int PASSWORD_LENGTH, String CHARACTER_SET)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        if (email == null || masterPassword == null) return null;
        if (CHARACTER_SET.equals("d"))
            CHARACTER_SET = DEFAULT_CHARACTER_SET;

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
