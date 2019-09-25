package token;

public class TokenFactory {

  public static Token create(Operator op, int line, int pos) {
    return new TokenOperator(op, line, pos);
  }

  public static Token create(Keyword kw, int line, int pos) {
    return new TokenKeyword(kw, line, pos);
  }

  public static Token create(Delimiter d, int line, int pos) {
    return new TokenDelimiter(d, line, pos);
  }

  public static Token create(double number, int line, int pos) {
    return new TokenLiteral(number, line, pos);
  }

  public static Token create(String identifier, int line, int pos) {
    return new TokenLiteral(identifier, line, pos);
  }

  public static Token create(Special spec, int line, int pos) {
    switch (spec) {
      case TokenEOF:
        return new TokenEOF(line, pos);
      case TokenInvalid:
        return new TokenInvalid(line, pos);
    }

    return null;
  }
}
