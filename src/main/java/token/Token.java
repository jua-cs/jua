package token;

import java.util.HashMap;
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

  public Keyword getKeywordFromLiteral(String literal) {
    HashMap<String, Keyword> lookUpTable = Keyword.getLookUpTable();
    return lookUpTable.get(literal);
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
