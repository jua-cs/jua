package lexer;

import token.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
        token = new TokenOperator(Operator.PLUS, currentLine, currentPosinLine);
        readChar();
        break;
      case '-':
        token = new TokenOperator(Operator.MINUS, currentLine, currentPosinLine);
        readChar();
        break;
      case '*':
        token = new TokenOperator(Operator.ASTERISK, currentLine, currentPosinLine);
        readChar();
        break;
      case '/':
        token = new TokenOperator(Operator.SLASH, currentLine, currentPosinLine);
        readChar();
        break;
      case '<':
        token = new TokenOperator(Operator.LT, currentLine, currentPosinLine);
        readChar();
        break;
      case '>':
        token = new TokenOperator(Operator.GT, currentLine, currentPosinLine);
        readChar();
        break;
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
        token = new InvalidToken(currentLine, currentPosinLine, "");
        readChar();
        break;
      default:
        if (Character.isLetter(ch)) {
          String identifier = nextIdentifier();
          token = new TokenLiteral(Literal.IDENTIFIER, identifier, currentLine, currentPosinLine);
        }

        if (Character.isDigit(ch)) {
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

  public String nextNumber() {
    String number = "";

    while (Character.isDigit(ch)) {
      number += Character.toString(ch);
      readChar();
    }

    return number;
  }
}
