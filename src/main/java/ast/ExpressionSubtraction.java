package ast;

import token.Token;

public abstract class ExpressionSubtraction extends ExpressionBinary {

  public ExpressionSubtraction(Token token, Expression valueLeft, Expression valueRight) {
    super(token, valueLeft, valueRight);
  }
}
