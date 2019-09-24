package token;

public class TokenFactory {

  public static Token create(Literal lit, String str, int line, int pos) {
    return new TokenLiteral(Literal.IDENTIFIER, str, line, pos);
  }

  public static Token create(Operator op, int line, int pos) {
    return new TokenOperator(op, line, pos);
  }

  public static Token create(Keyword kw, int line, int pos) {
    return new TokenKeyword(kw, line, pos);
  }

  public static Token create(Delimiter d, int line, int pos) {
    return new TokenDelimiter(d, line, pos);
  }

  public static Token createEOF(int line, int pos) {
    return new TokenEOF(line, pos);
  }

  public static Token createInvalid(int line, int pos, String lit) {
    return new TokenInvalid(line, pos, lit);
  }
}
