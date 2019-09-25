package ast;

import token.Token;

public class ExpressionDivision extends ExpressionBinary {

  public ExpressionDivision(Token token, Expression valueLeft, Expression valueRight) {
    super(token, valueLeft, valueRight);
  }
}
