package token;

public class TokenLiteral extends Token {

  private Literal literalType;
  private double numValue;

  protected TokenLiteral(String literal, int line, int position) {
    super(TokenType.LITERAL, literal, line, position);
    this.literalType = Literal.IDENTIFIER;
  }

  protected TokenLiteral(double number, int line, int position) {
    super(TokenType.LITERAL, Double.toString(number), line, position);
    this.literalType = Literal.NUMBER;
  }
}
