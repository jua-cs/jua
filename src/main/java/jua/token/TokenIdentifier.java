package jua.token;

public class TokenIdentifier extends Token {

  public static final String VariadicToken = "...";

  protected TokenIdentifier(String literal, int line, int position) {
    super(literal, line, position);
  }
}
