package ast;

import token.TokenOperator;

public class ExpressionLessThan extends ExpressionBinary {
  protected ExpressionLessThan(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
