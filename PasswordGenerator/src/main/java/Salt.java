import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class Salt {
    static byte[] getSalt()
            throws NoSuchAlgorithmException, NoSuchProviderException
    {
        // Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");

        // Create array for salt
        byte[] salt = new byte[16];

        // Get a random salt
        sr.nextBytes(salt);

        // return salt
        return salt;
    }
    static byte[] getSalt(byte[] bytes)
    {
        if (bytes.length < 16) return null;
        if (bytes.length == 16) return bytes;

        // Create array for salt
        byte[] salt = new byte[16];

        System.arraycopy(bytes, 0, salt, 0, 16);

        // return salt
        return salt;
    }
}
