package lexer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import token.*;

public class Lexer {

  private String in;
  private char ch;
  private int readPosition;
  private int currentLine;
  private int currentPosinLine;

  public Lexer(String in) {
    this.in = in;
  }



  private char peekChar() {
    if (readPosition >= in.length()) {
      return 0;
    }
    return in.charAt(readPosition);
  }


  private void readChar() {
    ch = peekChar();
    readPosition++;
    currentPosinLine++;
  }

  private void consumeWhitespace() throws IOException {
    while (ch == ' ' || ch == '\t' || ch == '\n') {
      if (ch == '\n') {
        currentPosinLine = 0;
        currentLine++;
      }
      readChar();
    }
  }

  public Token nextToken() throws IOException {
    Token token = null;

    consumeWhitespace();

    switch (ch) {
      case '=':
        token = new TokenOperator(Operator.EQUAL, currentLine, currentPosinLine);
        readChar();
        break;
      case '+':
      case '-':
      case '*':
      case '/':
      case '<':
      case '>':
      case '(':
      case ')':
      case '[':
      case ']':
      case '{':
      case '}':
      case ',':
      case ';':
      case '%':
      case '^':
      case '#':
      case ':':
      case '.':
      default:
        if (Character.isLetter(ch)) {
          String identifier = nextIdentifier();
          token = new TokenLiteral(Literal.IDENTIFIER, identifier, currentLine, currentPosinLine);
        }
    }

    return token;
  }

  public String nextIdentifier() {
    String identifier = "";

    while (Character.isLetter(ch)) {
      identifier += Character.toString(ch);
      readChar();
    }

    return identifier;
  }
}
