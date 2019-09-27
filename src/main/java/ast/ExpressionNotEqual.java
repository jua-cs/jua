package ast;

import token.TokenOperator;

public class ExpressionNotEqual extends ExpressionBinary {
  protected ExpressionNotEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
