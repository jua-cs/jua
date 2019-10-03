package jua.ast;

import jua.token.Operator;
import jua.token.TokenFactory;
import jua.token.TokenOperator;

public class ExpressionHash extends ExpressionUnary {

  ExpressionHash(TokenOperator token, Expression value) {
    super(TokenFactory.create(Operator.HASH, token.getLine(), token.getPosition()), value);
  }
}
