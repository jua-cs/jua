package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaBoolean;
import objects.LuaObject;
import token.TokenOperator;

public class ExpressionNotEqual extends ExpressionBinary {
  ExpressionNotEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws IllegalCastException {
    return new LuaBoolean(LuaBoolean.valueOf(lhs.evaluate(evaluator)) != LuaBoolean.valueOf(rhs.evaluate(evaluator)));
  }
}
