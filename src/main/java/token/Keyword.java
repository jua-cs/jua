package token;

public enum Keyword {
    DO("do"),
    END("do"),
    WHILE("while"),
    REPEAT("repeat"),
    UNTIL("until"),
    IF("if"),
    THEN("then"),
    ELSEIF("elseif"),
    ELSE("else"),
    FOR("for"),
    IN("in"),
    FUNCTION("function"),
    LOCAL("local"),
    RETURN("return"),
    BREAK("break"),
    NIL("nil"),
    FALSE("false"),
    TRUE("true");

    private String name;

    Keyword(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
