package ast;

import token.TokenOperator;

public class ExpressionAnd extends ExpressionBinary {
  protected ExpressionAnd(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
