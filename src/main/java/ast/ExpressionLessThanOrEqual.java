package ast;

import token.TokenOperator;

public class ExpressionLessThanOrEqual extends ExpressionBinary {
  protected ExpressionLessThanOrEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
