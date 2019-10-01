package ast;

import token.TokenOperator;

public class ExpressionOr extends ExpressionBinary {
  ExpressionOr(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
