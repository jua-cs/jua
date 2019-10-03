package ast;

import token.TokenOperator;

public class ExpressionAddition extends ExpressionBinary {

  ExpressionAddition(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
