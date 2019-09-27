package ast;

import token.TokenOperator;

public class ExpressionGreaterThan extends ExpressionBinary {
  protected ExpressionGreaterThan(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
