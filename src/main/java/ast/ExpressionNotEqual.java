package ast;

import token.TokenOperator;

public class ExpressionNotEqual extends ExpressionBinary {
  ExpressionNotEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
