package token;

import java.util.Objects;

public abstract class Token {
  private TokenType type;
  private int line;
  private int position;
  private String literal;

  Token(TokenType type, String literal, int line, int position) {
    this.type = type;
    this.line = line;
    this.position = position;
    this.literal = literal;
  }

  public static Token fromString(String literal, int currentLine, int currentPos) {
    Keyword keyword = Keyword.getKeywordTable().get(literal);
    if (keyword != null) {
      return TokenFactory.create(keyword, currentLine, currentPos);
    }

    // Keyword that is also an operator like 'and'
    Operator operator = Operator.getOpTable().get(literal);
    if (operator != null) {
      return TokenFactory.create(operator, currentLine, currentPos);
    }

    return TokenFactory.create(literal, currentLine, currentPos);
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getLiteral() {
    return literal;
  }

  public void setLiteral(String literal) {
    this.literal = literal;
  }

  public TokenType getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Token token = (Token) o;
    return line == token.line
        && position == token.position
        && type == token.type
        && Objects.equals(literal, token.literal);
  }

  @Override
  public String toString() {
    return "Token{"
        + "type="
        + type
        + ", line="
        + line
        + ", position="
        + position
        + ", literal='"
        + literal
        + '\''
        + '}';
  }
}
