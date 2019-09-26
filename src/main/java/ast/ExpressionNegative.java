package ast;

import token.Operator;
import token.Token;
import token.TokenFactory;

public class ExpressionNegative extends ExpressionUnary {

  public ExpressionNegative(Token token, Expression value) {
    super(TokenFactory.create(Operator.NEGATIVE, token.getLine(), token.getPosition()), value);
  }
}
