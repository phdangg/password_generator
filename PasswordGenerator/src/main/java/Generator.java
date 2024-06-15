import at.favre.lib.crypto.bcrypt.BCrypt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;


public class Generator {
    private final byte[] hash;
    private final int passwordLength;
    private final String template;

    Generator(String masterPassword, int passwordLength, ArrayList<CharacterGroup> options, String ...metadata){
        this.hash = hashing(masterPassword,metadata);
        this.passwordLength = passwordLength;
        // Fill the template with chosen characters
        this.template = generateTemplate(options);
        // Generate character set with chosen options
        CharacterSet.generateCharacterSet(options);
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
        assert salt16Bytes != null;
        return BCrypt.withDefaults().hash(10, salt16Bytes, masterPassword.getBytes(StandardCharsets.UTF_8));
    }
    //A template is a string generated for every password based
    //on the character set. E.g., a password with at least one
    //number, one lowercase character and a length of 10.
    // Whereas "n" denotes a number, "a" denotes a lowercase
    // letter and x denotes any symbol from the
    //character set.
    private String generateTemplate(ArrayList<CharacterGroup> options){
        StringBuilder template = new StringBuilder();
        for (CharacterGroup op : options) {
            template.append(CharacterGroup.mapCharacterGroupToTemplate(op));
        }
        template.append("x".repeat(Math.max(0, passwordLength - template.length())));

        return shuffleTemplate(template.toString());
    }
    // Fisher-Yates shuffle algorithm,
    // the randomness is derived from a BigInteger created from a hash value
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

    public String getPassword() {
        Map<Character, String> templateToCharacterSetMap = CharacterSet.mapCharacterSet();

        StringBuilder finalPassword = new StringBuilder();
        // Deterministically choose characters from CHARACTER_SET
        int indexOfTemplate = 0;
        for (byte b : hash) {
            if (finalPassword.length() > passwordLength - 1) {
                break;
            }
            char templateChar = template.charAt(indexOfTemplate);
            String charSet = templateToCharacterSetMap.get(templateChar);
            finalPassword.append(charSet.charAt(b % charSet.length()));
            indexOfTemplate++;
        }

        return finalPassword.toString();
    }

    @Override
    public String toString() {
        return getPassword();
    }
}
