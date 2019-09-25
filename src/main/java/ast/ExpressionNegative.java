package ast;

import token.Token;

public class ExpressionNegative extends ExpressionUnary {

  public ExpressionNegative(Token token, Expression value) {
    super(token, value);
  }
}
