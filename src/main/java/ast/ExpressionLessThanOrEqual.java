package ast;

import token.TokenOperator;

public class ExpressionLessThanOrEqual extends ExpressionBinary {
  ExpressionLessThanOrEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
