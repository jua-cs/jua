package token;

public class TokenDelimiter extends Token {
  private Delimiter delimiter;

  TokenDelimiter(int line, int position, Delimiter delimiter) {
    super(TokenType.DELIMITER, line, position, delimiter.toString());
    this.delimiter = delimiter;
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }
}
