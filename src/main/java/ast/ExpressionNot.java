package ast;

import token.Token;

public class ExpressionNot extends ExpressionUnary {

  public ExpressionNot(Token token, Expression value) {
    super(token, value);
  }
}
