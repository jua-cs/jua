package ast;

import token.Token;

public abstract class ExpressionBinary extends Expression {

  private Expression valueLeft;
  private Expression valueRight;

  public ExpressionBinary(Token token, Expression valueLeft, Expression valueRight) {
    super(token);
  }
}
