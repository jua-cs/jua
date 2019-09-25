package ast;

import token.Token;

public abstract class ExpressionUnary extends Expression {

  private Expression value;

  public ExpressionUnary(Token token, Expression value) {
    super(token);
  }
}
