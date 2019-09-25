package token;

public class TokenInvalid extends Token {
  protected TokenInvalid(int line, int position) {
    super(TokenType.INVALID, "", line, position);
  }
}
