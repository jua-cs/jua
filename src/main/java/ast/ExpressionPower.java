package ast;

import token.TokenOperator;

public class ExpressionPower extends ExpressionBinary {
  protected ExpressionPower(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}