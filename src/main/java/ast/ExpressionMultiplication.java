package ast;

import token.Token;

public class ExpressionMultiplication extends ExpressionBinary {

  public ExpressionMultiplication(Token token, Expression valueLeft, Expression valueRight) {
    super(token, valueLeft, valueRight);
  }
}
