package ast;

import token.TokenOperator;

public class ExpressionNot extends ExpressionUnary {

  protected ExpressionNot(TokenOperator token, Expression value) {
    super(token, value);
  }
}
