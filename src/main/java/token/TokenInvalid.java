package token;

public class TokenInvalid extends Token {
  protected TokenInvalid(int line, int position, String litteral) {
    super(TokenType.INVALID, litteral, line, position);
  }
}
