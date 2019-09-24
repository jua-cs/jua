package token;

public class InvalidToken extends Token {
  public InvalidToken(int line, int position, String litteral) {
    super(TokenType.INVALID, litteral, line, position);
  }
}
