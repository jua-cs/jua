package token;

public abstract class Token {
  private TokenType type;
  private int line;
  private int position;
  private String litteral;

  Token(TokenType type, String litteral, int line, int position) {
    this.type = type;
    this.line = line;
    this.position = position;
    this.litteral = litteral;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getLitteral() {
    return litteral;
  }

  public void setLitteral(String litteral) {
    this.litteral = litteral;
  }

  public TokenType getType() {
    return type;
  }
}
