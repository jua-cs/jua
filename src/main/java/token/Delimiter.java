package token;

public enum Delimiter {
  COMMA(","),
  SEMICOLON(";"),
  LPAREN("("),
  RPAREN(")"),
  LBRACE("{"),
  RBRACE("}");

  private String repr;

  Delimiter(String repr) {
    this.repr = repr;
  }

  @Override
  public String toString() {
    return this.repr;
  }
}
