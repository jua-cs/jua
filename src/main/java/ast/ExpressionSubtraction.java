package ast;

import token.TokenOperator;

public class ExpressionSubtraction extends ExpressionBinary {

  protected ExpressionSubtraction(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
