package ast;

import token.Token;

public class ExpressionIdentifier extends ExpressionUnary {

  public ExpressionIdentifier(Token token, Expression value) {
    super(token, value);
  }
}
