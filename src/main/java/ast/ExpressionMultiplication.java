package ast;

import token.TokenOperator;

public class ExpressionMultiplication extends ExpressionBinary {

  ExpressionMultiplication(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
