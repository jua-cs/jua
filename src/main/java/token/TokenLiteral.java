package token;

public class TokenLiteral extends Token {

  private Literal literalType;

  protected TokenLiteral(Literal literalType, String literal, int line, int position) {
    super(TokenType.LITERAL, literal, line, position);
    this.literalType = literalType;
  }

  public Literal getLiteralType() {
    return literalType;
  }
}
