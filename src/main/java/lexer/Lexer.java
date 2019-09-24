package lexer;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import token.*;

public class Lexer {

  private String in;
  private char ch;
  private int readPosition;
  private int currentLine;
  private int currentPosinLine;

  public Lexer(String in) {
    this.currentLine = 0;
    this.in = in;
    readChar();
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

  private void consumeWhitespace() {
    while (ch == ' ' || ch == '\t' || ch == '\n') {
      if (ch == '\n') {
        currentPosinLine = 0;
        currentLine++;
      }
      readChar();
    }
  }

  public Token nextToken() {
    Token token = null;

    consumeWhitespace();
    // Store it now in the cas of a multi char token
    int currentPos = currentPosinLine;

    System.out.printf("Char: %c, pos: %d\n", ch, currentPos);
    switch (ch) {
      case 0:
        token = new EOFToken(currentLine, currentPos);
        break;
      case '=':
        if (peekChar() == '=') {
          token = new TokenOperator(Operator.EQUALS, currentLine, currentPos);
          readChar();
        } else {
          token = new TokenOperator(Operator.ASSIGN, currentLine, currentPos);
        }
        break;
      case '+':
        token = new TokenOperator(Operator.PLUS, currentLine, currentPos);
        break;
      case '-':
        // Handle comments
        if (peekChar() == '-') {
          nextLine();
          return nextToken();
        } else {
          token = new TokenOperator(Operator.MINUS, currentLine, currentPos);
        }
        break;
      case '*':
        token = new TokenOperator(Operator.ASTERISK, currentLine, currentPos);
        break;
      case '/':
        token = new TokenOperator(Operator.SLASH, currentLine, currentPos);
        break;
      case '<':
        if (peekChar() == '=') {
          token = new TokenOperator(Operator.LTE, currentLine, currentPos);
          readChar();
        } else {
          token = new TokenOperator(Operator.LT, currentLine, currentPos);
        }
        break;
      case '>':
        if (peekChar() == '=') {
          token = new TokenOperator(Operator.GTE, currentLine, currentPos);
          readChar();
        } else {
          token = new TokenOperator(Operator.GT, currentLine, currentPos);
        }
        break;
      case '(':
        token = new TokenDelimiter(Delimiter.LPAREN, currentLine, currentPos);
        break;
      case ')':
        token = new TokenDelimiter(Delimiter.RPAREN, currentLine, currentPos);
        break;
      case '[':
        token = new TokenDelimiter(Delimiter.LBRACK, currentLine, currentPos);
        break;
      case ']':
        token = new TokenDelimiter(Delimiter.RBRACK, currentLine, currentPos);
        break;
      case '{':
        token = new TokenDelimiter(Delimiter.LBRACE, currentLine, currentPos);
        break;
      case '}':
        token = new TokenDelimiter(Delimiter.RBRACE, currentLine, currentPos);
        break;
      case ',':
        token = new TokenDelimiter(Delimiter.COMMA, currentLine, currentPos);
        break;
      case ';':
        token = new TokenDelimiter(Delimiter.SEMICOLON, currentLine, currentPos);
        break;
      case '%':
        token = new TokenOperator(Operator.PERCENT, currentLine, currentPos);
        break;
      case '^':
        token = new TokenOperator(Operator.CARAT, currentLine, currentPos);
        break;
      case '#':
        token = new TokenDelimiter(Delimiter.HASH, currentLine, currentPos);
        break;
      case ':':
        token = new TokenDelimiter(Delimiter.COLON, currentLine, currentPos);
        break;
      case '.':
        token = new TokenOperator(Operator.DOT, currentLine, currentPos);
        break;
      default:
        if (Character.isLetter(ch)) {
          String identifier = nextIdentifier();

          Keyword keyword = Token.getKeywordFromLiteral(identifier);
          if (keyword != null) {
            return new TokenKeyword(keyword, currentLine, currentPos);
          }

          // return early to avoid readChar below
          return new TokenLiteral(Literal.IDENTIFIER, identifier, currentLine, currentPos);

        } else if (Character.isDigit(ch)) {
          String number = nextNumber();

          // return early to avoid readChar below
          return new TokenLiteral(Literal.NUMBER, number, currentLine, currentPos);
        }

        token = new InvalidToken(currentLine, currentPos, "");
    }

    readChar();

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

  public void nextLine() {
    while (ch != '\n') {
      readChar();
    }
    readChar();
    consumeWhitespace();
  }

  public String nextNumber() {
    String number = "";
    boolean dotSeen = false;
    while (Character.isDigit(ch) || (!dotSeen && ch == '.')) {
      number += Character.toString(ch);
      readChar();
      dotSeen = ch == '.';
    }

    return number;
  }

  public ArrayList<Token> getNTokens(int n) {
    if (n <= 0) {
      return null;
    }

    Stream<Token> stream = Stream.generate(this::nextToken).limit(n);
    return stream.collect(Collectors.toCollection(ArrayList::new));
  }
}
