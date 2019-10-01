package ast;

import token.TokenOperator;

public class ExpressionSubtraction extends ExpressionBinary {

  ExpressionSubtraction(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
