package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaBoolean;
import jua.token.TokenOperator;

public class ExpressionAnd extends ExpressionBinary {
  ExpressionAnd(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws LuaRuntimeException {
    return LuaBoolean.getLuaBool(
        LuaBoolean.valueOf(lhs.evaluate(evaluator)).getValue()
            && LuaBoolean.valueOf(rhs.evaluate(evaluator)).getValue());
  }
}
