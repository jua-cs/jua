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
  HASH("#"),
  COLUMN(":");

  private String repr;

  Delimiter(String repr) {
    this.repr = repr;
  }

  @Override
  public String toString() {
    return this.repr;
  }

  public static HashMap<String, Delimiter> getLookUpTable() {
    HashMap<String, Delimiter> lookUpTable = new HashMap<>();
    for (Delimiter e : Delimiter.values()) {
      lookUpTable.put(e.toString(), e);
    }
    return lookUpTable;
  }
}
