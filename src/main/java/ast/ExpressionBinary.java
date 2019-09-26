package ast;

import token.Token;
import token.TokenOperator;

public abstract class ExpressionBinary extends Expression {

  private Expression lhs;
  private Expression rhs;

  protected ExpressionBinary(TokenOperator token, Expression lhs, Expression rhs) {
    super(token);
    this.lhs = lhs;
    this.rhs = rhs;
  }

  @Override
  public String toString() {
    return String.format("%s BinaryOp(%s) %s", lhs, token, rhs);
  }
}
