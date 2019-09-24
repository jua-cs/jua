package token;

public class TokenDelimiter extends Token {
  private Delimiter delimiter;

  TokenDelimiter(int line, int position, Delimiter delimiter) {
    super(TokenType.DELIMITER, delimiter.toString(), line, position);
    this.delimiter = delimiter;
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }
}
