package ast;

import token.TokenOperator;

public class ExpressionEquals extends ExpressionBinary {
  protected ExpressionEquals(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
