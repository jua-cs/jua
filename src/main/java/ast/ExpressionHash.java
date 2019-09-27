package ast;

import token.Operator;
import token.TokenFactory;
import token.TokenOperator;

public class ExpressionHash extends ExpressionUnary {

  protected ExpressionHash(TokenOperator token, Expression value) {
    super(TokenFactory.create(Operator.HASH, token.getLine(), token.getPosition()), value);
  }
}
