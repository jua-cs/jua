package ast;

import token.TokenOperator;

public class ExpressionOr extends ExpressionBinary {
  protected ExpressionOr(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
