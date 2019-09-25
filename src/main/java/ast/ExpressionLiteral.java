package ast;

import token.Token;

public class ExpressionLiteral extends ExpressionUnary {

  public ExpressionLiteral(Token token, Expression value) {
    super(token, value);
  }
}
