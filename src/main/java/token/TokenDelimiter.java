package token;

public class TokenDelimiter extends Token {
  private Delimiter delimiter;


  TokenDelimiter(Delimiter delimiter, int line, int position) {
    super(TokenType.DELIMITER, delimiter.toString(), line, position);
    this.delimiter = delimiter;
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }
}
