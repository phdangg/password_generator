import at.favre.lib.crypto.bcrypt.BCrypt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Generator {
    private final ArrayList<CharacterGroup> options;
    private final byte[] hash;
    private final int passwordLength;
    private String template;
    private String CHARACTER_SET;

    Generator(String masterPassword, int passwordLength, ArrayList<CharacterGroup> options, String ...metadata){
        this.hash = hashing(masterPassword,metadata);
        this.passwordLength = passwordLength;
        this.options = options;
        // Fill the template with chosen characters
        this.template = generateTemplate();
        // Generate character set with chosen options
        this.CHARACTER_SET = CharacterSet.generateCharacterSet(options);
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
        template.append("x".repeat(Math.max(0, passwordLength - template.length())));

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

    public String getPassword() {

        StringBuilder finalPassword = new StringBuilder();
        Map<Character, String> templateToCharacterSetMap = new HashMap<>();
        templateToCharacterSetMap.put('x', CHARACTER_SET);
        templateToCharacterSetMap.put('a', CharacterSet.LOWER);
        templateToCharacterSetMap.put('A', CharacterSet.UPPER);
        templateToCharacterSetMap.put('s', CharacterSet.SYMBOL);
        templateToCharacterSetMap.put('n', CharacterSet.NUMBER);

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
