import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PBKDF2 {
    public static byte[] getPBKDF2WithSalt(String masterPassword, byte[] salt){
        try {
            int iterations = 1000;
            char[] chars = masterPassword.toCharArray();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64*8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = skf.generateSecret(spec).getEncoded();
            return hash;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }
    }
}
