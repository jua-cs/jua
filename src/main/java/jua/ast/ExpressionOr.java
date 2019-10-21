package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaBoolean;
import jua.objects.LuaObject;
import jua.token.TokenOperator;

public class ExpressionOr extends ExpressionBinary {
  ExpressionOr(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaObject lValue = lhs.evaluate(scope);
    if (LuaBoolean.valueOf(lValue).getValue()) {
      return lValue;
    }
    return rhs.evaluate(scope);
  }
}
