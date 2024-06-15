public enum CharacterGroup {
    UPPER,LOWER,NUMBER,SYMBOL;
    public static String mapCharacterGroupToTemplate(CharacterGroup op) {
        switch (op) {
            case LOWER:
                return "a";
            case NUMBER:
                return "n";
            case SYMBOL:
                return "s";
            case UPPER:
                return "A";
            default:
                return "";
        }
    }
}
