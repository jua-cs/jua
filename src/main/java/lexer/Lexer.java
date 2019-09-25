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
  private int currentPosInLine;

  public Lexer(String in) {
    this.currentLine = 1;
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
    currentPosInLine++;
  }

  private void consumeWhitespace() {
    while (ch == ' ' || ch == '\t' || ch == '\n') {
      if (ch == '\n') {
        currentPosInLine = 0;
        currentLine++;
      }
      readChar();
    }
  }

  public Token nextToken() {
    Token token;

    consumeWhitespace();
    // Store it now in the case of a multi char token
    int currentPos = currentPosInLine;

    System.out.printf("Char: '%c', pos: %d\n", ch, currentPos);
    switch (ch) {
      case 0:
        token = TokenFactory.create(Special.TokenEOF, currentLine, currentPos);
        break;
      case '=':
        if (peekChar() == '=') {
          token = TokenFactory.create(Operator.EQUALS, currentLine, currentPos);
          readChar();
        } else {
          token = TokenFactory.create(Operator.ASSIGN, currentLine, currentPos);
        }
        break;
      case '+':
        token = TokenFactory.create(Operator.PLUS, currentLine, currentPos);
        break;
      case '-':
        // Handle comments
        if (peekChar() == '-') {
          nextLine();
          return nextToken();
        } else {
          token = TokenFactory.create(Operator.MINUS, currentLine, currentPos);
        }
        break;
      case '*':
        token = TokenFactory.create(Operator.ASTERISK, currentLine, currentPos);
        break;
      case '/':
        token = TokenFactory.create(Operator.SLASH, currentLine, currentPos);
        break;
      case '<':
        if (peekChar() == '=') {
          token = TokenFactory.create(Operator.LTE, currentLine, currentPos);
          readChar();
        } else {
          token = TokenFactory.create(Operator.LT, currentLine, currentPos);
        }
        break;
      case '>':
        if (peekChar() == '=') {
          token = TokenFactory.create(Operator.GTE, currentLine, currentPos);
          readChar();
        } else {
          token = TokenFactory.create(Operator.GT, currentLine, currentPos);
        }
        break;
      case '(':
        token = TokenFactory.create(Delimiter.LPAREN, currentLine, currentPos);
        break;
      case ')':
        token = TokenFactory.create(Delimiter.RPAREN, currentLine, currentPos);
        break;
      case '[':
        token = TokenFactory.create(Delimiter.LBRACK, currentLine, currentPos);
        break;
      case ']':
        token = TokenFactory.create(Delimiter.RBRACK, currentLine, currentPos);
        break;
      case '{':
        token = TokenFactory.create(Delimiter.LBRACE, currentLine, currentPos);
        break;
      case '}':
        token = TokenFactory.create(Delimiter.RBRACE, currentLine, currentPos);
        break;
      case ',':
        token = TokenFactory.create(Delimiter.COMMA, currentLine, currentPos);
        break;
      case ';':
        token = TokenFactory.create(Delimiter.SEMICOLON, currentLine, currentPos);
        break;
      case '%':
        token = TokenFactory.create(Operator.PERCENT, currentLine, currentPos);
        break;
      case '^':
        token = TokenFactory.create(Operator.CARAT, currentLine, currentPos);
        break;
      case '#':
        token = TokenFactory.create(Delimiter.HASH, currentLine, currentPos);
        break;
      case ':':
        token = TokenFactory.create(Delimiter.COLON, currentLine, currentPos);
        break;
      case '.':
        token = TokenFactory.create(Operator.DOT, currentLine, currentPos);
        break;
      default:
        if (Character.isLetter(ch)) {
          String identifier = nextIdentifier();
          // return early to avoid readChar below
          return TokenFactory.create(identifier, currentLine, currentPos);
        } else if (Character.isDigit(ch)) {
          double number = nextNumber();

          // return early to avoid readChar below
          return TokenFactory.create(number, currentLine, currentPos);
        }

        token = TokenFactory.create(Special.TokenInvalid, currentLine, currentPos);
    }

    readChar();

    return token;
  }

  public String nextIdentifier() {
    StringBuilder identifier = new StringBuilder();

    while (Character.isLetter(ch)) {
      identifier.append(ch);
      readChar();
    }

    return identifier.toString();
  }

  public void nextLine() {
    while (ch != '\n') {
      readChar();
    }
    currentLine++;
    currentPosInLine = 0;
    readChar();
    consumeWhitespace();
  }

  public double nextNumber() {
    StringBuilder number = new StringBuilder();
    boolean dotSeen = false;
    while (Character.isDigit(ch) || (!dotSeen && ch == '.')) {
      number.append(ch);
      readChar();
      dotSeen = ch == '.';
    }

    return Double.parseDouble(number.toString());
  }

  public ArrayList<Token> getNTokens(int n) {
    if (n <= 0) {
      ArrayList<Token> tokens = new ArrayList<>();
      Token tok;
      do {
        tok = nextToken();
        tokens.add(tok);
      } while (tok != null && tok.getType() != TokenType.EOF);
      return tokens;
    }

    Stream<Token> stream = Stream.generate(this::nextToken).limit(n);
    return stream.collect(Collectors.toCollection(ArrayList::new));
  }
}
