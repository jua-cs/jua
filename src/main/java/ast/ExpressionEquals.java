package ast;

import token.TokenOperator;

public class ExpressionEquals extends ExpressionBinary {
  ExpressionEquals(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
