package ast;

import token.TokenOperator;

public class ExpressionConcatenation extends ExpressionBinary {
  ExpressionConcatenation(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }
}
