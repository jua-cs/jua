package token;

import java.util.HashMap;

public enum Operator {
  PLUS("+"),
  MINUS("-"),
  ASTERISK("*"),
  SLASH("/"),
  CARAT("^"),
  PERCENT("%"),
  LT("<"),
  GT(">"),
  POINT("."),
  EQUAL("=");

  private String repr;

  Operator(String repr) {
    this.repr = repr;
  }

  @Override
  public String toString() {
    return this.repr;
  }

  public static HashMap<String, Operator> getLookUpTable() {
    HashMap<String, Operator> lookUpTable = new HashMap<>();
    for (Operator e : Operator.values()) {
      lookUpTable.put(e.toString(), e);
    }
    return lookUpTable;
  }
}
