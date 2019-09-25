package ast;

import token.TokenOperator;

public class ExpressionNot extends ExpressionUnary {

  public ExpressionNot(TokenOperator token, Expression value) {
    super(token, value);
  }
}
