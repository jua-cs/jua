package token;

import java.util.HashMap;

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
}
