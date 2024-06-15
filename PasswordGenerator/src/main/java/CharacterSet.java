import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CharacterSet {
    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String SYMBOL = "#!\"~|@^°$%&/()[]{}=-_+*<>;:.";
    public static final String NUMBER = "0123456789";
    public static String CHARACTER_SET;
    public static String mapCharacterGroupToCharacterSet(CharacterGroup op) {
        return switch (op) {
            case LOWER -> CharacterSet.LOWER;
            case NUMBER -> CharacterSet.NUMBER;
            case SYMBOL -> CharacterSet.SYMBOL;
            case UPPER -> CharacterSet.UPPER;
            default -> "";
        };
    }
    public static String generateCharacterSet(ArrayList<CharacterGroup> options){
        StringBuilder characterSet = new StringBuilder();
        for (CharacterGroup op : options) {
            characterSet.append(CharacterSet.mapCharacterGroupToCharacterSet(op));
        }
        return characterSet.toString();
    }
    public static Map<Character,String> mapCharacterSet(){
        Map<Character, String> templateToCharacterSetMap = new HashMap<>();
        templateToCharacterSetMap.put('x', CHARACTER_SET);
        templateToCharacterSetMap.put('a', LOWER);
        templateToCharacterSetMap.put('A', UPPER);
        templateToCharacterSetMap.put('s', SYMBOL);
        templateToCharacterSetMap.put('n', NUMBER);
        return templateToCharacterSetMap;
    }
}
