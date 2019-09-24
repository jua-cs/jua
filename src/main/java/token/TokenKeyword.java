package token;

public class TokenKeyword extends Token {
  private Keyword keyword;

  protected TokenKeyword(Keyword keyword, int line, int position) {
    super(TokenType.KEYWORD, keyword.toString(), line, position);
    this.keyword = keyword;
  }

  public Keyword getKeyword() {
    return keyword;
  }
}
