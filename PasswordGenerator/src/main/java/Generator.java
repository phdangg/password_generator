import at.favre.lib.crypto.bcrypt.BCrypt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;


public class Generator {
    private final ArrayList<CharacterGroup> options;
    private final byte[] hash;
    private int passwordLength;
    private static final String DEFAULT_CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?/~`";

    Generator(String masterPassword, int passwordLength, ArrayList<CharacterGroup> options, String ...metadata){
        this.hash = hashing(masterPassword,metadata);
        this.passwordLength = passwordLength;
        this.options = options;
    }
    private byte[] hashing(String masterPassword, String ...metadata){
        // Get salt
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
    private String generateTemplate(){
        StringBuilder template = new StringBuilder();
        for (CharacterGroup op : options) {
            template.append(CharacterGroup.mapCharacterGroupToTemplate(op));
        }
        for (int i = 0; i < passwordLength - template.length(); i++) {
            template.append("x");
        }

        return shuffleTemplate(template.toString());
    }
    private String shuffleTemplate(String s) {

        BigInteger bigInt = new BigInteger(hash);

        int index;
        char temp;
        char[] array = s.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            BigInteger tempInt = BigInteger.valueOf(i);
            BigInteger[] divAndMod = bigInt.divideAndRemainder(tempInt);
            bigInt = divAndMod[0];
            index = divAndMod[1].intValue();
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return String.valueOf(array);
    }

    private String generateCharacterSet(){
        StringBuilder characterSet = new StringBuilder();
        for (CharacterGroup op : options) {
            characterSet.append(CharacterSet.mapCharacterGroupToCharacterSet(op));
        }
        return characterSet.toString();
    }

    public String getPassword() {
        // Fill the template with chosen characters
        String template = generateTemplate();

        // Generate Character set
        String CHARACTER_SET = generateCharacterSet();

        StringBuilder finalPassword = new StringBuilder();
        int indexOfTemplate = 0;
        // Deterministically choose characters from CHARACTER_SET
        for (byte b : hash){
            if (finalPassword.length() > passwordLength - 1) {
                break;
            }
            int index = b % hash.length;
            if (template.charAt(indexOfTemplate) == 'x'){
                finalPassword.append(CHARACTER_SET.charAt(index));
            } else if (template.charAt(indexOfTemplate) == 'a') {
                finalPassword.append(CharacterSet.LOWER.charAt(index));
            } else if (template.charAt(indexOfTemplate) == 'A') {
                finalPassword.append(CharacterSet.UPPER.charAt(index));
            } else if (template.charAt(indexOfTemplate) == 's') {
                finalPassword.append(CharacterSet.SYMBOL.charAt(index));
            } else if (template.charAt(indexOfTemplate) == 'n') {
                finalPassword.append(CharacterSet.NUMBER.charAt(index));
            }
        }

        return finalPassword.toString();
    }

    @Override
    public String toString() {
        return getPassword();
    }
}
