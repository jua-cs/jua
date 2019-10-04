package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaBoolean;
import jua.token.TokenOperator;

public class ExpressionAnd extends ExpressionBinary {
  ExpressionAnd(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Scope scope) throws LuaRuntimeException {
    return LuaBoolean.getLuaBool(
        LuaBoolean.valueOf(lhs.evaluate(scope)).getValue()
            && LuaBoolean.valueOf(rhs.evaluate(scope)).getValue());
  }
}
