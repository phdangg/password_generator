public class CharacterSet {
    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String SYMBOL = "#!\"~|@^°$%&/()[]{}=-_+*<>;:.";
    public static final String NUMBER = "0123456789";
    public static String mapCharacterGroupToCharacterSet(CharacterGroup op) {
        switch (op) {
            case LOWER:
                return CharacterSet.LOWER;
            case NUMBER:
                return CharacterSet.NUMBER;
            case SYMBOL:
                return CharacterSet.SYMBOL;
            case UPPER:
                return CharacterSet.UPPER;
            default:
                return "";
        }
    }
}
