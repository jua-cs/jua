package ast;

import token.TokenOperator;

public class ExpressionGreaterThanOrEqual extends ExpressionBinary {
  ExpressionGreaterThanOrEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
