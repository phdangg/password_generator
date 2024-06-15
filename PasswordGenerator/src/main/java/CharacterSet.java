import java.util.ArrayList;

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
        CHARACTER_SET = characterSet.toString();
        return CHARACTER_SET;
    }
}
