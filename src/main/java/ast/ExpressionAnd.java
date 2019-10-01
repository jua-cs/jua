package ast;

import token.TokenOperator;

public class ExpressionAnd extends ExpressionBinary {
  ExpressionAnd(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
