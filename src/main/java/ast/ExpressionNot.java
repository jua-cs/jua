package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaBoolean;
import token.Operator;
import token.TokenFactory;
import token.TokenOperator;

public class ExpressionNot extends ExpressionUnary {

  ExpressionNot(TokenOperator token, Expression value) {
    super(TokenFactory.create(Operator.NOT, token.getLine(), token.getPosition()), value);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws IllegalCastException {
    return new LuaBoolean(!LuaBoolean.valueOf(value.evaluate(evaluator)).getValue());
  }
}
