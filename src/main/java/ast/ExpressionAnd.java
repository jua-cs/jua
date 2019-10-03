package ast;

import evaluator.Evaluator;
import objects.LuaBoolean;
import objects.LuaObject;
import token.TokenOperator;

public class ExpressionAnd extends ExpressionBinary {
  ExpressionAnd(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) {
    return new LuaBoolean(
        LuaBoolean.valueOf(lhs.evaluate(evaluator)).getValue()
            && LuaBoolean.valueOf(rhs.evaluate(evaluator)).getValue());
  }
}
