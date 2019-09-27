package ast;

import token.TokenOperator;

public class ExpressionModulo extends ExpressionBinary {
  protected ExpressionModulo(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
