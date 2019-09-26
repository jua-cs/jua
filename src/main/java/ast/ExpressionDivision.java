package ast;

import token.TokenOperator;

public class ExpressionDivision extends ExpressionBinary {

  protected ExpressionDivision(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
