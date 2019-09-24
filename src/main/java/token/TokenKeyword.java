package token;

public class TokenKeyword extends Token {
  private Keyword keyword;

  TokenKeyword(int line, int position, Keyword keyword) {
    super(TokenType.KEYWORD, keyword.toString(), line, position);
    this.keyword = keyword;
  }

  public Keyword getKeyword() {
    return keyword;
  }
}
