package ast;

import token.Token;

public class ExpressionHash extends ExpressionUnary {

  public ExpressionHash(Token token, Expression value) {
    super(token, value);
  }
}
