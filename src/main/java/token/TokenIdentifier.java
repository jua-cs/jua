package token;

public class TokenIdentifier extends Token {

  protected TokenIdentifier(String literal, int line, int position) {
    super(TokenType.IDENTIFIER, literal, line, position);
  }
}
