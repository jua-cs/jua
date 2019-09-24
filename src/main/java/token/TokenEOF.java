package token;

public class TokenEOF extends Token {

  protected TokenEOF(int line, int position) {
    super(TokenType.EOF, "EOF", line, position);
  }
}
