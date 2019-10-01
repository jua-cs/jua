package token;

import java.util.Objects;

public abstract class Token {
  private TokenType type;
  private int line;
  private int position;
  private String literal;

  Token(TokenType type, String literal, int line, int position) {
    this.type = type;
    this.line = line;
    this.position = position;
    this.literal = literal;
  }

  public static Token fromString(String literal, int currentLine, int currentPos) {
    if (literal.equals("true") || literal.equals("false")) {
      return TokenFactory.create(Literal.BOOLEAN, literal, currentLine, currentPos);
    }

    if (literal.equals("nil")) {
      return TokenFactory.create(Literal.NIL, literal, currentLine, currentPos);
    }

    Keyword keyword = Keyword.getKeywordTable().get(literal);
    if (keyword != null) {
      return TokenFactory.create(keyword, currentLine, currentPos);
    }

    // Keyword that is also an operator like 'and'
    Operator operator = Operator.getOpTable().get(literal);
    if (operator != null) {
      return TokenFactory.create(operator, currentLine, currentPos);
    }

    return TokenFactory.create(literal, currentLine, currentPos);
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

  public String getLiteral() {
    return literal;
  }

  public void setLiteral(String literal) {
    this.literal = literal;
  }

  public TokenType getType() {
    return type;
  }

  public boolean isSubtype(Delimiter delimiter) {
    if (!(this instanceof TokenDelimiter)) {
      return false;
    }
    TokenDelimiter tok = (TokenDelimiter) this;
    return (tok.getDelimiter() == delimiter);
  }

  public boolean isSubtype(Operator op) {

    if (!(this instanceof TokenOperator)) {
      return false;
    }

    TokenOperator tok = (TokenOperator) this;

    return (tok.getOperator() == op);
  }

  public boolean isSubtype(Keyword kw) {

    if (!(this instanceof TokenKeyword)) {
      return false;
    }

    TokenKeyword tok = (TokenKeyword) this;

    return (tok.getKeyword() == kw);
  }

  public boolean lightEquals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Token token = (Token) o;
    return type == token.type && Objects.equals(literal, token.literal);
  }

  @Override
  public final boolean equals(Object o) {
    // Since we implement the comparison for subclass here, we make it final
    if (this == o) return true;
    if (!(o.getClass() == getClass())) return false;
    Token token = (Token) o;
    if (line != token.line) return false;
    if (position != token.position) return false;
    if (type != token.type) return false;
    if (!Objects.equals(literal, token.literal)) return false;

    // Because most equals() calls are done between token, but we want to differentiate two TokenDelimiter
    if (o instanceof TokenDelimiter) {
      TokenDelimiter target = (TokenDelimiter) token;
      TokenDelimiter current = (TokenDelimiter) this;
      return target.getDelimiter() == current.getDelimiter();
    } else if (o instanceof TokenKeyword) {
      TokenKeyword target = (TokenKeyword) token;
      TokenKeyword current = (TokenKeyword) this;
      return target.getKeyword() == current.getKeyword();
    } else if (o instanceof TokenOperator) {
      TokenOperator target = (TokenOperator) token;
      TokenOperator current = (TokenOperator) this;
      return target.getOperator() == current.getOperator();
      }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, literal);
  }

  @Override
  public String toString() {
    return "Token{"
        + "type="
        + type
        + ", line="
        + line
        + ", position="
        + position
        + ", literal='"
        + literal
        + '\''
        + '}';
  }
}
