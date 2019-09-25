package ast;

import token.Token;

public abstract class ExpressionLiteral extends ExpressionUnary {

  public ExpressionLiteral(Token token, Expression value) {
    super(token, value);
  }
}
