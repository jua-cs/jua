package jua.token;

public class TokenLiteral extends Token {

  private Literal literalType;

  protected TokenLiteral(Literal literalType, String literal, int line, int position) {
    super(literal, line, position);
    this.literalType = literalType;
  }

  public Literal getLiteralType() {
    return literalType;
  }
}
