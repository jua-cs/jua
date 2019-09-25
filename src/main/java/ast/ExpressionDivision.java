package ast;

import token.Token;

public abstract class ExpressionDivision extends ExpressionBinary {

  public ExpressionDivision(Token token, Expression valueLeft, Expression valueRight) {
    super(token, valueLeft, valueRight);
  }
}
