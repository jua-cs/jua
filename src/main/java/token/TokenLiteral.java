package token;

public class TokenLiteral extends Token {
    
    private Literal literalType;


    TokenLiteral(int line, int position, Literal literalType, String literal) {
        super(TokenType.LITERAL, line, position, literal);
        this.literalType = literalType;
    }
}
