package ast;

import token.Token;

public class ExpressionAddition extends ExpressionBinary {

  public ExpressionAddition(Token token, Expression valueLeft, Expression valueRight) {
    super(token, valueLeft, valueRight);
  }
}
