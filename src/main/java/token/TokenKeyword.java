package token;

public class TokenKeyword extends Token {
    private Keyword keyword;

    TokenKeyword(int line, int position, Keyword keyword) {
        super(TokenType.KEYWORD, line, position, keyword.toString());
    }

    public Keyword getKeyword() {
        return keyword;
    }
}
