package token;

public class TokenDelimiter extends Token {
  private Delimiter delimiter;

  protected TokenDelimiter(Delimiter delimiter, int line, int position) {
    super(delimiter.toString(), line, position);
    this.delimiter = delimiter;
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    TokenDelimiter that = (TokenDelimiter) o;

    return delimiter == that.delimiter;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + delimiter.hashCode();
    return result;
  }
}
