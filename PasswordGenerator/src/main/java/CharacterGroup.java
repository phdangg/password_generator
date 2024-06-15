public enum CharacterGroup {
    UPPER,LOWER,NUMBER,SYMBOL;
    public static String mapCharacterGroupToTemplate(CharacterGroup op) {
        return switch (op) {
            case LOWER -> "a";
            case NUMBER -> "n";
            case SYMBOL -> "s";
            case UPPER -> "A";
        };
    }
}
