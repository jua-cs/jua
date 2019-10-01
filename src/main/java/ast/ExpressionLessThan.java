package ast;

import token.TokenOperator;

public class ExpressionLessThan extends ExpressionBinary {
  ExpressionLessThan(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
