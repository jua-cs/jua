package token;

import java.util.HashMap;

public enum Keyword {
  DO("do"),
  END("end"),
  WHILE("while"),
  REPEAT("repeat"),
  UNTIL("until"),
  IF("if"),
  THEN("then"),
  ELSEIF("elseif"),
  ELSE("else"),
  FOR("for"),
  IN("in"),
  FUNCTION("function"),
  LOCAL("local"),
  RETURN("return"),
  BREAK("break"),
  NIL("nil"),
  FALSE("false"),
  TRUE("true");

  private String repr;

  Keyword(String repr) {
    this.repr = repr;
  }

  @Override
  public String toString() {
    return this.repr;
  }

  public static HashMap<String, Keyword> getKeywordTable() {
    // TODO compute only once with a static field
    HashMap<String, Keyword> lookUpTable = new HashMap<>();
    for (Keyword e : Keyword.values()) {
      lookUpTable.put(e.toString(), e);
    }
    return lookUpTable;
  }
}
