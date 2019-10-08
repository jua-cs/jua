package jua.lexer;

import java.util.ArrayList;
import jua.token.*;
import util.BufferedChannel;

public class Lexer {

  private BufferedChannel<Character> in;
  private char ch;
  public boolean init = false;
  private int readPosition;
  private int currentLine;
  private int currentPosInLine;

  public Lexer(BufferedChannel<Character> in) {
    this.currentLine = 1;
    this.in = in;
  }

  public Lexer(String in) {
    this.currentLine = 1;
    this.in = BufferedChannel.fromString(in);
  }

  private char peekChar() {
    return in.peek();
  }

  private void readChar() {
    try {
      ch = in.read();
    } catch (InterruptedException e) {
      // TODO: handle this
      e.printStackTrace();
    }
    currentPosInLine++;
  }

  private void consumeWhitespace() {
    while (Character.isWhitespace(ch)) {
      if (ch == '\n') {
        currentPosInLine = 0;
        currentLine++;
      }
      readChar();
    }
  }

  public Token nextToken() {
    if (!init) {
      init = true;
      readChar();
    }

    Token token;

    consumeWhitespace();

    // Store it now in the case of a multi char jua.token
    int currentPos = currentPosInLine;

    switch (ch) {
      case 0:
        // Stop on EOF
        return TokenFactory.create(Special.TokenEOF, currentLine, currentPos);
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
        token = TokenFactory.create(Operator.HASH, currentLine, currentPos);
        break;
      case ':':
        token = TokenFactory.create(Delimiter.COLON, currentLine, currentPos);
        break;
      case '\'':
      case '"':
        // TODO more extended support for strings like:
        // a = 'alo\n123"'
        //     a = "alo\n123\""
        //     a = '\97lo\10\04923"'
        //     a = [[alo
        //     123"]]
        //     a = [==[
        //     alo
        //     123"]==]

        return TokenFactory.create(Literal.STRING, readStringLiteral(), currentLine, currentPos);
      case '.':
        if (peekChar() == '.') {
          token = TokenFactory.create(Operator.CONCAT, currentLine, currentPos);
          readChar();
        } else {
          token = TokenFactory.create(Operator.DOT, currentLine, currentPos);
        }
        break;
      case '~':
        if (peekChar() == '=') {
          token = TokenFactory.create(Operator.NOT_EQUAL, currentLine, currentPos);
          readChar();
          break;
        }
        // fallthrough
      default:
        if (Character.isLetter(ch) || ch == '_') {
          String identifier = nextIdentifier();
          // return early to avoid readChar below
          return Token.fromString(identifier, currentLine, currentPos);
        } else if (Character.isDigit(ch)) {
          String number = nextNumber();

          // return early to avoid readChar below
          return TokenFactory.create(Literal.NUMBER, number, currentLine, currentPos);
        }

        token = TokenFactory.create(Special.TokenInvalid, currentLine, currentPos);
    }

    // Move to next char
    readChar();

    return token;
  }

  private String nextIdentifier() {
    // from Lua manual : Names (also called identifiers) in Lua can be any string of letters,
    // digits, and underscores, not beginning with a digit.
    StringBuilder identifier = new StringBuilder();

    // (Character.isLetter(ch) || ch == '_') is true
    identifier.append(ch);
    readChar();

    while (Character.isLetterOrDigit(ch) || ch == '_') {
      identifier.append(ch);
      readChar();
    }

    return identifier.toString();
  }

  private void nextLine() {
    while (ch != '\n') {
      readChar();
    }
    currentLine++;
    currentPosInLine = 0;
    readChar();
    consumeWhitespace();
  }

  private String readStringLiteral() {
    // Handle both ' and "
    char sep = ch;
    readChar();
    StringBuilder str = new StringBuilder();

    while (ch != sep && ch != 0) {
      str.append(ch);
      readChar();
    }
    readChar();

    return str.toString();
  }

  private String nextNumber() {
    StringBuilder number = new StringBuilder();
    boolean dotSeen = false;
    while (Character.isDigit(ch) || (!dotSeen && ch == '.')) {
      number.append(ch);
      dotSeen = ch == '.';
      readChar();
    }

    return number.toString();
  }

  public ArrayList<Token> getNTokens(int n) {
    ArrayList<Token> tokens = new ArrayList<>();
    Token tok;

    do {
      tok = nextToken();
      tokens.add(tok);
    } while ((tok != null && !(tok instanceof TokenEOF) && (n <= 0 || n > tokens.size())));
    return tokens;
  }
}
