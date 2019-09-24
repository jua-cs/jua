package lexer;

public class Token {
  private int line;
  private int position;
  private String litteral;

  Token(int line, int position, String litteral) {
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
}
