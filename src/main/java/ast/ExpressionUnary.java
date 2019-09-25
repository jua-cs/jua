package ast;

import token.TokenOperator;

public abstract class ExpressionUnary extends Expression {

  private Expression value;

  public ExpressionUnary(TokenOperator token, Expression value) {
    super(token);
    this.value = value;
  }
}
