package jua.lexer;

import java.util.ArrayList;
import jua.evaluator.IllegalLexingException;
import jua.token.*;
import util.BufferedChannel;

public class Lexer {

  private BufferedChannel<Character> in;
  private BufferedChannel<Token> out = new BufferedChannel<>();
  private char ch;
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
    Character c = null;
    try {
      c = in.peek();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (c != null) {
      return c;
    }
    return 0;
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
    // lex newlines as tokens
    while (Character.isWhitespace(ch) && ch != '\n') {
      readChar();
    }
  }

  public Token nextToken() throws IllegalLexingException {
    readChar();

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
        if (peekChar() == '<') {
          token = TokenFactory.create(Operator.LEFT_SHIFT, currentLine, currentPos);
          readChar();
        } else if (peekChar() == '=') {
          token = TokenFactory.create(Operator.LTE, currentLine, currentPos);
          readChar();
        } else {
          token = TokenFactory.create(Operator.LT, currentLine, currentPos);
        }
        break;
      case '>':
        if (peekChar() == '>') {
          token = TokenFactory.create(Operator.RIGHT_SHIFT, currentLine, currentPos);
          readChar();
        } else if (peekChar() == '=') {
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
        token = TokenFactory.create(Operator.COLON, currentLine, currentPos);
        break;
      case '\n':
        token = TokenFactory.create(Delimiter.NEWLINE, currentLine, currentPos);
        currentLine++;
        currentPosInLine = 0;
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
          readChar();
          if (peekChar() == '.') {
            readChar();
            token = Token.fromString(TokenIdentifier.VariadicToken, currentLine, currentPos);
          } else {
            token = TokenFactory.create(Operator.CONCAT, currentLine, currentPos);
          }
        } else {
          token = TokenFactory.create(Operator.DOT, currentLine, currentPos);
        }
        break;
      case '&':
        token = TokenFactory.create(Operator.B_AND, currentLine, currentPos);
        break;
      case '|':
        token = TokenFactory.create(Operator.B_OR, currentLine, currentPos);
        break;
      case '~':
        if (peekChar() == '=') {
          token = TokenFactory.create(Operator.NOT_EQUAL, currentLine, currentPos);
          readChar();
        } else {
          token = TokenFactory.create(Operator.B_XOR, currentLine, currentPos);
        }
        break;
        // fallthrough
      default:
        if (Character.isLetter(ch) || ch == '_') {
          String identifier = nextIdentifier();
          // return early to avoid readChar below
          return Token.fromString(identifier, currentLine, currentPos);
        } else if (Character.isDigit(ch)) {
          // check if it's hexadecimal
          if (ch == '0' && peekChar() == 'x') {
            // it's an hexadecimal
            String numberHex = nextHexadecimal();
            return TokenFactory.create(Literal.HEX_NUMBER, numberHex, currentLine, currentPos);
          }
          String number = nextNumber();

          // return early to avoid readChar below
          return TokenFactory.create(Literal.NUMBER, number, currentLine, currentPos);
        }

        token = TokenFactory.create(Special.TokenInvalid, currentLine, currentPos);
    }

    return token;
  }

  private String nextIdentifier() {
    // from Lua manual : Names (also called identifiers) in Lua can be any string of letters,
    // digits, and underscores, not beginning with a digit.
    StringBuilder identifier = new StringBuilder();

    // (Character.isLetter(ch) || ch == '_') is true
    identifier.append(ch);
    char nextChar = peekChar();

    while (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
      identifier.append(nextChar);
      readChar();
      nextChar = peekChar();
    }

    return identifier.toString();
  }

  private void nextLine() {
    while (ch != '\n') {
      readChar();
    }
    currentLine++;
    currentPosInLine = 0;
  }

  private String readStringLiteral() throws IllegalLexingException {
    // Handle both ' and "
    char sep = ch;
    char nextChar = peekChar();
    StringBuilder str = new StringBuilder();

    while (nextChar != sep && nextChar != 0) {
      readChar();
      // Check for escaped quotes
      if (nextChar == '\\') {
        if (peekChar() == sep) {
          str.append(sep);
          readChar();
        } else {
          str.append(nextChar);
          str.append(peekChar());
          readChar();
        }
      } else {
        str.append(nextChar);
      }
      nextChar = peekChar();
    }

    if (nextChar == '\0') {
      throw new IllegalLexingException(String.format("unexpected end of file in string literal"));
    }

    readChar();

    return str.toString();
  }

  private String nextNumber() {
    StringBuilder number = new StringBuilder();
    number.append(ch);
    boolean dotSeen = false;
    char nextChar = peekChar();
    while (Character.isDigit(nextChar) || (!dotSeen && nextChar == '.')) {
      number.append(nextChar);
      dotSeen = nextChar == '.';
      readChar();
      nextChar = peekChar();
    }

    return number.toString();
  }

  private String nextHexadecimal() {
    StringBuilder number = new StringBuilder();
    number.append(ch); // ch = 0
    readChar();
    number.append(ch); // ch = x

    boolean dotSeen = false;
    char nextChar = peekChar();
    while (Character.isLetterOrDigit(nextChar) || (!dotSeen && nextChar == '.')) {
      // invalid hex with g are raised at evaluation
      number.append(nextChar);
      dotSeen = nextChar == '.';
      readChar();
      nextChar = peekChar();
    }

    return number.toString();
  }

  public ArrayList<Token> getNTokens(int n) {
    ArrayList<Token> tokens = new ArrayList<>();
    Token tok = null;

    do {
      try {
        tok = nextToken();
        tokens.add(tok);
      } catch (IllegalLexingException e) {
        e.printStackTrace();
      }
    } while ((tok != null && !(tok instanceof TokenEOF) && (n <= 0 || n > tokens.size())));
    return tokens;
  }

  public BufferedChannel<Token> getOut() {
    return out;
  }

  public void start(boolean isInteractive) throws InterruptedException, IllegalLexingException {
    while (true) {
      try {
        Token token = nextToken();
        out.add(token);
        if (token == null || token instanceof TokenEOF) {
          break;
        }
      } catch (IllegalLexingException e) {
        if (!isInteractive) {
          out.add(TokenFactory.create(Special.TokenEOF, currentLine, currentPosInLine));
        }
        throw e;
      }
    }
  }
}
