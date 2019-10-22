package jua.token;

import java.util.HashMap;

public enum Operator {
  PLUS("+"),
  MINUS("-"),
  ASTERISK("*"),
  SLASH("/"),
  CARAT("^"),
  PERCENT("%"),
  DOT("."),
  COLON(":"),
  CONCAT(".."),
  ASSIGN("="),
  LT("<"),
  GT(">"),
  EQUALS("=="),
  LTE("<="),
  GTE(">="),
  NOT_EQUAL("~="),
  AND("and"),
  OR("or"),
  INDEX("["),
  B_AND("&"),
  B_OR("|"),
  B_XOR("~"),
  RIGHT_SHIFT(">>"),
  LEFT_SHIFT("<<"),

  NEGATIVE("-", Arity.UNARY),
  HASH("#", Arity.UNARY),
  NOT("not", Arity.UNARY),
  B_NOT("~", Arity.UNARY);

  private String repr;
  private Arity arity = Arity.BINARY;

  Operator(String repr) {
    this.repr = repr;
  }

  Operator(String repr, Arity arity) {
    this.repr = repr;
    this.arity = arity;
  }

  public static HashMap<String, Operator> getOpTable() {
    // TODO compute only once with a static field
    HashMap<String, Operator> lookUpTable = new HashMap<>();
    for (Operator e : Operator.values()) {
      lookUpTable.put(e.toString(), e);
    }
    return lookUpTable;
  }

  public Arity getArity() {
    return arity;
  }

  @Override
  public String toString() {
    return this.repr;
  }
}
