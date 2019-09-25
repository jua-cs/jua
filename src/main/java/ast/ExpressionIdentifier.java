package ast;

import token.Token;

public abstract class ExpressionIdentifier extends ExpressionUnary {

  public ExpressionIdentifier(Token token, Expression value) {
    super(token, value);
  }
}
