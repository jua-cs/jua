package ast;

import token.TokenOperator;

public class ExpressionGreaterThanOrEqual extends ExpressionBinary {
  protected ExpressionGreaterThanOrEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
