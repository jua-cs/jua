package token;

public class EOFToken extends Token {

  EOFToken(int line, int position) {
    super(TokenType.EOF, "EOF", line, position);
  }
}
