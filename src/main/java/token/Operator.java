package token;

public enum Operator {
    PLUS("+"),
    MINUS("-"),
    ASTERISK("*"),
    SLASH("/"),
    CARAT("^"),
    LT("<"),
    GT(">");

    private String name;

    Operator(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
