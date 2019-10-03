package ast;

import evaluator.Evaluator;
import objects.LuaBoolean;
import objects.LuaObject;
import token.Operator;
import token.TokenFactory;
import token.TokenOperator;

public class ExpressionNot extends ExpressionUnary {

  ExpressionNot(TokenOperator token, Expression value) {
    super(TokenFactory.create(Operator.NOT, token.getLine(), token.getPosition()), value);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) {
    return new LuaBoolean(!LuaBoolean.valueOf(value.evaluate(evaluator)).getValue());
  }
}
