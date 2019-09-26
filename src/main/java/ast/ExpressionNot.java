package ast;

import token.Operator;
import token.TokenFactory;
import token.TokenOperator;

public class ExpressionNot extends ExpressionUnary {

  protected ExpressionNot(TokenOperator token, Expression value) {
    super(TokenFactory.create(Operator.NOT, token.getLine(), token.getPosition()), value);
  }
}
