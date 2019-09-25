package token;

import java.util.HashMap;

public enum Delimiter {
  COMMA(","),
  SEMICOLON(";"),
  LPAREN("("),
  RPAREN(")"),
  LBRACE("{"),
  RBRACE("}"),
  LBRACK("["),
  RBRACK("]"),
  COLON(":");

  private String repr;

  Delimiter(String repr) {
    this.repr = repr;
  }

  public static HashMap<String, Delimiter> getLookUpTable() {
    HashMap<String, Delimiter> lookUpTable = new HashMap<>();
    for (Delimiter e : Delimiter.values()) {
      lookUpTable.put(e.toString(), e);
    }

    return lookUpTable;
  }

  public boolean matches(Delimiter other) {
    switch (this) {
      case LPAREN:
        return other == RPAREN;
      case RPAREN:
        return other == LPAREN;
      case LBRACE:
        return other == RBRACE;
      case RBRACE:
        return other == LBRACE;
      case LBRACK:
        return other == RBRACK;
      case RBRACK:
        return other == LBRACK;
      default:
        return false;
    }
  }

  @Override
  public String toString() {
    return this.repr;
  }
}
