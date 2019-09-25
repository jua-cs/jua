package ast;

import token.Token;

public abstract class ExpressionNot extends Expression {

  private Expression value;

  public ExpressionNot(Token token, Expression value) {
    super(token);
  }
}
